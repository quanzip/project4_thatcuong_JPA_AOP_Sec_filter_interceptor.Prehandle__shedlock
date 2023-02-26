package com.viettel.project.service.schedule;

import com.viettel.project.service.AuthorityService;
import com.viettel.project.service.BillItemService;
import com.viettel.project.service.dto.BillItemDTO;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaSendCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.charset.Charset;
import java.util.List;

@Component
public class ScheduledTasks {
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final KafkaSendCallback<String, Object> kafkaSendCallback = new KafkaSendCallback<String, Object>() {
        @Override
        public void onFailure(KafkaProducerException e) {
            logger.info("Failed to send event", e);
        }

        @Override
        public void onSuccess(SendResult<String, Object> result) {
            logger.info("Success to send event to kafka: " + result.getRecordMetadata().topic() + ", value: " + result.getProducerRecord().value());
        }
    };
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private BillItemService billItemService;

    // s m h d M Thứ
    @Scheduled(cron = "0 0 0/3 * * *")
    @SchedulerLock(name = "clean_bill_item", lockAtLeastFor = 2000, lockAtMostFor = 4000)
    public void deleteBillHasZeroItem() {
        List<BillItemDTO> billItemDTOS = billItemService.deleteBillItemThatHasZeroQuantity();
        if (billItemDTOS.size() > 0)
            billItemDTOS.forEach(billItemDTO -> kafkaTemplate.send("del-billitem", billItemDTO).addCallback(kafkaSendCallback));
    }


//      https://www.baeldung.com/rest-template
//      https://www.baeldung.com/how-to-use-resttemplate-with-basic-authentication-in-spring

//    @Scheduled(cron = "0 0/1 * * * *")
//    @SchedulerLock(name = "import_paths_and_roles", lockAtMostFor = 40000, lockAtLeastFor = 20000)
    public void importPathsAndRoles(){
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity httpEntity = new HttpEntity(createHeaders("quanzip", "123"));
        ResponseEntity<Integer> result = restTemplate.exchange("http://localhost:9081/sapis", HttpMethod.GET, httpEntity, Integer.class);
        if (result.getStatusCodeValue() == 200){
            logger.info("\n----------------------------re-import all apis and role to Database------------------------");
        }
    }

    // call Api with basic authen úing rést tepmlate
    HttpHeaders createHeaders(String username, String password) {
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                    auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
        }};
    }

}

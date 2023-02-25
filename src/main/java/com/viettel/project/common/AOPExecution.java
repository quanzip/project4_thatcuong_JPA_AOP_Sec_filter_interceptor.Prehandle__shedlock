package com.viettel.project.common;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


/*
 * We can use @aspect to write log, delete cache, check authority\
 * Không sử dụng try-catch để bắt lỗi trong đây khi ProceedingJoinPoint.proceed()
 * vì @ControllerAdvice sẽ không bắt được lỗi đó, thay vì thế ta để method văng lỗi tự nhiên
 * như bên dưới
 * */
// all method a running properly
@Component
@Aspect
public class AOPExecution {
    private final Logger logger = LoggerFactory.getLogger(AOPExecution.class);

//    Truoc khi cac METHOD cua cac class trong package controller dc goi, no se di qua ham nay truoc
//    KHONG tinh cac class la sub-package cua controller neu phia sau controller chi co 1 dau cham
//    vd: * com.viettel.project.controller.*.*(..)

//    Neu phia sau controller co 2 dau cham, Ham nay se dc goi app dung khi goi cac method cua class
//    ben trong package controller va ca class ben trong sub-package cua controller.
//    vd: * com.viettel.project.controller..*.*(..)

    @Before(value = "(execution(* com.viettel.project.controller..*.*(..)))")
    public void beforeExecution(JoinPoint joinPoint) throws Throwable {
        logger.info("Before exe, joinPoint:  " + joinPoint);
    }

//    Sau khi cac method dc chay ma co tham so dau tien la long, cac tham so sau khong co hoac bat ke type nao
//    thi deu chay qua ham nay cuoi cung

    @After(value = "(execution(* com.viettel.project.controller.*.*(Long,..)))")
    public void afterMethodExecution(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Object thiss = joinPoint.getThis();
        logger.info("After method executions for method has first param type LONG, target: " + target + " , this: " + thiss);

    }

//    @Around(): Được dùng để intercept vào đầu và cuối của method,
//    ta có thể dùng Joinpoint.proceed()
//    để tiếp tực thực thi

    @Around(value = "(execution(* com.viettel.project.service.*.*(..)))")
    public Object usingAroundToLogAndCatchTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("bat dau chay vao ham vao luc: " + LocalDateTime.now());
        Object re = null;
//             thuc thi ham chinh, result la kq tra ve cua function
        re = proceedingJoinPoint.proceed();


        logger.info("Ham chinh da chay xong luc: " + LocalDateTime.now());

//        In ra Kq cua ham chinh thuc thi o day
        logger.info("Gia tri tra ve cua ham chinh: " + re);
        return re;
    }

    // neu de .. la ap dung cho ca class trong package service va cac sub package
//    @AfterReturning(value = "(execution(* com.viettel.project.service..*.*()))")
//    public void afterReturnning(JoinPoint joinPoint){
//        logger.info("After returnnign");
//    }

    //Bat het cac calls toi cac phuong thuc trong cac class cua package common va sub-folder cuar common
    @Pointcut(value = "(execution(* com.viettel.project.common..*.*(..)))")
    public void catchAllEventFromCommonPackage() {
    }

    @Around(value = "catchAllEventFromCommonPackage()")
    public Object catchUtilsUsingPointCut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("Start runnning method in fileUtils");
        Object object = proceedingJoinPoint.proceed();
        logger.info("Finish runnning method in fileUtils");

        return object;
    }

    @Pointcut(value = "within(com.viettel.project..*)")
    public void catchTypeUsingWithin() {
    }

    @After(value = "catchTypeUsingWithin()")
    public void catchTypeUsingPointCut(JoinPoint joinPoint) throws Throwable {
        logger.info("After runnning method in package project using within");
    }


    //    bat ky method nao start with 'search' va co 1 tham so Long dau tien, theo sau la
//    //    Bat ky tham so nao
    @Pointcut(value = "execution(* *..search*(Long,..))")
    public void catchFuncStartWithSearch() {
    }

    @Before(value = "catchFuncStartWithSearch()")
    public void beforeRunFunctionStartBySearch() {
        logger.info("Before run function start by search!!");
    }


    /*
    * CAN NOT UNCOMMENT BELOW FUNCTIONS BECAUSE THEY CAUSE PROBLEMS
    * THEY ARE TOO COMMON FOR SPRING TO KNOW WHAT EXACTLY POINTCUT SHOULD BE LISTENED
    * */


//
//    //    bat ky method nao cos tu 'save' o ten method
////    rui ro: taat cac cac ham nhu service.save...   repo.save...  , fileUtils.save...
////    deu trigger this method
//    @Pointcut(value = "execution(* *..*Handler*(..))")
//    public void catchFuncHasSaveInName() {
//    }
//
//    @Before(value = "catchFuncHasSaveInName()")
//    public void beforeRunFunctionHasSaveInName() {
//        logger.info("Before run function Has 'save' in functionName");
//    }
//
//
//    /*
//     * We can combine predicate @Pointcut together:
//     * combination: phai la method to package common va trong ten ham co 'save'
//     * */
//    @Pointcut(value = "catchAllEventFromCommonPackage() || catchFuncHasSaveInName()")
//    public void eventFromCommonPakcageAndHasSaveInName() {
//    }
//
//    @After(value = "eventFromCommonPakcageAndHasSaveInName()")
//    public void catchCombination(JoinPoint joinPoint) {
//        logger.info("Catch: eventFromCommonPakcageAndHasSaveInName");
//    }




}

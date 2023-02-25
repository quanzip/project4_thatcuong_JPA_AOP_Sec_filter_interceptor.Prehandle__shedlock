package com.viettel.project.service.impl;

import com.viettel.project.common.AppProperty;
import com.viettel.project.common.FileUtils;
import com.viettel.project.entity.Product;
import com.viettel.project.repository.ProductRepository;
import com.viettel.project.service.ProductService;
import com.viettel.project.service.dto.ProductDTO;
import com.viettel.project.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.io.File;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AppProperty appProperty;

    @Override
    public List<ProductDTO> searchProduct(String search, Integer page, Integer size) {
        logger.info("Getting all products... done!");
        Pageable pageable = PageRequest.of(Objects.isNull(page) ? 0 : page, Objects.isNull(size) ? 10 : size);

        List<ProductDTO> res;
        Page<Product> products;
        if (Objects.isNull(search) || search.isEmpty()) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.searchProducts("%" + search + "%", pageable);
        }
        res = productMapper.toDTOS(products.getContent());
        return res;
    }

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        if (productDTO == null) throw new NullPointerException("could not save null product!");

        String image = null;
        File imageFolder = null;
        MultipartFile file = productDTO.getFile();
        if (file != null && file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
            image = FileUtils.genNewFileNameFromFile(file);
            imageFolder = new File(appProperty.getProductImageFolder()  + "/" + image);
            FileUtils.saveFileToFolder(file, imageFolder);
            productDTO.setImage(image);
        }

        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);

        if (!Objects.isNull(image)) {
            FileUtils.saveFileToFolder(file, imageFolder);
        }
        return productMapper.toDTO(product);
    }

    @Override
    public void updateProduct(ProductDTO productDTO) {
        // when updadte infomation of oldProduct, system should send email to all user, who bought this oldProduct before
        Long id = productDTO.getId();
        Product oldProduct = checkExistAndGetProduct(id);
        String oldImage = oldProduct.getImage();

        MultipartFile file = productDTO.getFile();
        String newImageName = null;
        // save new image file to folder
        if (!Objects.isNull(file) && !Objects.isNull(file.getOriginalFilename()) && !file.getOriginalFilename().isEmpty()) {
            newImageName = FileUtils.genNewFileNameFromFile(file);
            File newImageFile = new File(appProperty.getProductImageFolder()  + "/" + newImageName);
            FileUtils.saveFileToFolder(file, newImageFile);
            productDTO.setImage(newImageName);
        }

        //save Product information
        Product newProduct = productMapper.toEntity(productDTO);
        productRepository.save(newProduct);

        // delete old oldProduct image from image folder
        if (!Objects.isNull(oldImage)) {
            File oldIMage = new File(appProperty.getProductImageFolder() + "/" + oldImage);
            FileUtils.deleteFile(oldIMage);
        }
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = checkExistAndGetProduct(id);
        productRepository.delete(product);

        // delete image
        String image = product.getImage();
        File file = new File(appProperty.getProductImageFolder() + "/" + image);
        FileUtils.deleteFile(file);
    }

    @Override
    public Product checkExistAndGetProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NoResultException("non-exist product by Id: " + productId));
    }
}

package com.in.ecommerce.serviceImpl.admin;

import com.in.ecommerce.daoImpl.AdminCategoryDaoImpl;
import com.in.ecommerce.daoImpl.AdminProductDaoImpl;
import com.in.ecommerce.daoImpl.ProductDaoImpl;
import com.in.ecommerce.dto.FAQDto;
import com.in.ecommerce.dto.ProductDto;
import com.in.ecommerce.service.admin.AdminProductService;
import com.in.ecommerce.service.admin.CategoryService;
import com.in.ecommerce.wrapper.ProductWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminProductServiceImpl implements AdminProductService {

    private final AdminProductDaoImpl adminProductDaoImpl;
    private final ProductDaoImpl productDaoImpl;

    @Autowired
    private CategoryService categoryService;
    @Override
    public ProductWrapper addProduct(ProductDto productDto) throws IOException {
        ProductDto newProductDto= adminProductDaoImpl.addProduct(productDto);
        ProductWrapper productWrapper=new ProductWrapper();
        productWrapper.setId(newProductDto.getId());
        productWrapper.setName(newProductDto.getName());
        productWrapper.setDescription(newProductDto.getDescription());
        productWrapper.setPrice(newProductDto.getPrice());
        productWrapper.setByteImage(newProductDto.getByteImage());
        productWrapper.setCategoryId(newProductDto.getCategoryId());
        String categoryName=categoryService.getCategoryNameById(productDto.getCategoryId());
        productWrapper.setCategoryName(categoryName);

        return productWrapper;
    }

    @Override
    public List<ProductWrapper> getAllProducts() {
        return adminProductDaoImpl.getAllProducts();
    }

    @Override
    public List<ProductWrapper> getProductByName(String name) {
        return adminProductDaoImpl.getAllProductsByName(name);
    }

    @Override
    public boolean deleteProductById(Long productId) {
        int deletedRowCount=adminProductDaoImpl.deleteProductById(productId);
        log.info("Number of product deleted with Id : "+productId+" is -> "+deletedRowCount);
        if(deletedRowCount==1) return true;

        return false;
    }

    @Override
    public FAQDto postFAQ(Long productId, FAQDto faqDto) {
        faqDto.setProductId(productId);
        adminProductDaoImpl.postFAQ(faqDto);

        faqDto.setId(adminProductDaoImpl.findIdByQuestionandAnswerandProductId(faqDto));

        return faqDto;
    }

    @Override
    public ProductWrapper getProductById(Long productId) {
        Optional<ProductWrapper> optionalProductWrapper=productDaoImpl.findById(productId);
        if(optionalProductWrapper.isPresent()){
            return optionalProductWrapper.get();
        }

        return null;
    }

    @Override
    public ProductWrapper updateProduct(Long productId, ProductDto productDto) throws IOException {
        Optional<ProductWrapper> optionalProductWrapper=productDaoImpl.findById(productId);
        if(optionalProductWrapper.isPresent()){
            ProductDto productDtoNew=new ProductDto();
            if(productDto.getName()!=null && !productDto.getName().isEmpty()){
                productDtoNew.setName(productDto.getName());
            }

            if(productDto.getPrice()!=null){
                productDtoNew.setPrice(productDto.getPrice());
            }

            if(productDto.getDescription()!=null && !productDto.getDescription().isEmpty()){
                productDtoNew.setDescription(productDto.getDescription());
            }

            if(productDto.getCategoryId()!=null){
                productDtoNew.setCategoryId(productDto.getCategoryId());
            }

            if(productDto.getImage()!=null){
                productDtoNew.setImage(productDto.getImage());
            }

            adminProductDaoImpl.updateProductById(productId,productDtoNew);

            Optional<ProductWrapper> updatedProductWrapper=productDaoImpl.findById(productId);
            return updatedProductWrapper.get();

        }

        return null;
    }
}

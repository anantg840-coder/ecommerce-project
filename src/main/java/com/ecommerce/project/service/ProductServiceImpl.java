package com.ecommerce.project.service;

import com.ecommerce.project.Exception.ApiException;
import com.ecommerce.project.Exception.ResourceNotFoundException;
import com.ecommerce.project.PayLoad.CartDTO;
import com.ecommerce.project.PayLoad.ProductDTO;
import com.ecommerce.project.PayLoad.ProductResponse;
import com.ecommerce.project.Repositries.CartRepository;
import com.ecommerce.project.Repositries.CategoryRepositries;
import com.ecommerce.project.Repositries.ProductRepositories;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductServiceImpl implements ProductService{

    @Autowired
    private CartService cartService;


    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CategoryRepositries categoryRepositries;

    @Autowired
    private ProductRepositories productRepositories;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Category category = categoryRepositries.findById(categoryId).
                orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId", categoryId));
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for (Product value : products){
            if (value.getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }
        if (isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);

            product.setCategory(category);
            product.setImage("default.png");
            double specialPrice = product.getPrice() - (product.getPrice() * product.getDiscounts() * 0.01);
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepositories.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else{
            throw new ApiException("Product already exists");
        }




    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepositories.findAll(pageDetails);

        List<Product> productList = productPage.getContent();
        List<ProductDTO> productDTOS = productList.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());


        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setLastPage(productPage.isLast());



        return productResponse;




    }

    @Override
    public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepositries.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", "categoryId",categoryId));

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepositories.findByCategoryOrderByPriceAsc(pageDetails,category);

        List<Product> productList = productPage.getContent();

        List<ProductDTO> productDTOS = productList.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setLastPage(productPage.isLast());
        return productResponse;



    }

    @Override
    public ProductResponse searchProductByKeywords(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productPage = productRepositories.findByProductNameLikeIgnoreCase('%' + keyword + '%' ,pageDetails);
        List<Product> productList = productPage.getContent();



        List<ProductDTO> productDTOS = productList.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        if(productList.isEmpty()){
            throw new ApiException("Product not found");

        }






        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setPageNumber(pageNumber);
        productResponse.setPageSize(pageSize);
        productResponse.setLastPage(productPage.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct( ProductDTO productDTO, Long productId) {
        Product productfromDb = productRepositories.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId",productId));

        Product product = modelMapper.map(productfromDb,Product.class);

        productfromDb.setProductName(product.getProductName());
        productfromDb.setDescription(product.getDescription());
        productfromDb.setQuantity(product.getQuantity());
        productfromDb.setDiscounts(product.getDiscounts());
        productfromDb.setPrice(product.getPrice());
        productfromDb.setSpecialPrice(product.getSpecialPrice());

        Product savedProducts = productRepositories.save(productfromDb);

        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        List<CartDTO> cartDTOS = carts.stream()
                .map(cart-> {
                    CartDTO cartDTO = modelMapper.map(cart,CartDTO.class);
                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(p->modelMapper.map(p.getProduct(),ProductDTO.class))
                            .collect(Collectors.toList());
                    cartDTO.setProducts(productDTOS);
                    return cartDTO;
                }).collect(Collectors.toList());

        cartDTOS.forEach(cart -> cartService.updateProductInCarts(cart.getCartId(),productId));

        return modelMapper.map(savedProducts,ProductDTO.class);






    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product = productRepositories.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product", "productId", productId));
        List<Cart> carts = cartRepository.findCartsByProductId(productId);
        carts.forEach(cart ->
                cartService.deleteProductFromCart(cart.getCartId(),productId));


         productRepositories.delete(product);
        return modelMapper.map(product,ProductDTO.class);


    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb = productRepositories.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        String fileName = fileService.uploadImage(path,image);
        productFromDb.setImage(fileName);
        Product updatedProduct = productRepositories.save(productFromDb);
        return modelMapper.map(updatedProduct,ProductDTO.class);
        







    }


}

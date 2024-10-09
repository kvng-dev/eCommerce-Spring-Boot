package e_commerce.demo.service;

import e_commerce.demo.dto.ProductDTO;
import e_commerce.demo.dto.ProductListDTO;
import e_commerce.demo.mapper.ProductMapper;
import e_commerce.demo.model.Product;
import e_commerce.demo.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO, MultipartFile image) throws Exception {
        Product product = productMapper.toEntity(productDTO);
        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image);
            product.setImage("/images/" + fileName);
        }
        Product saved = productRepository.save(product);
        return productMapper.toDTO(saved);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO, MultipartFile image) throws Exception {

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResolutionException("Product not found"));
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantity(productDTO.getQuantity());
        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image);
            existingProduct.setImage("/images/" + fileName);
        }
        Product updated = productRepository.save(existingProduct);
        return productMapper.toDTO(updated);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResolutionException("Product not found"));
        productRepository.delete(existingProduct);
    }

    public ProductDTO getProduct(Long id) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(()-> new ResolutionException("Product not found"));
       return productMapper.toDTO(existingProduct);
    }

    public Page<ProductListDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAllWithoutComments(pageable);

    }

    private String saveImage(MultipartFile image) throws Exception {
        String filename = UUID.randomUUID().toString()+"_"+image.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR+ filename);
        Files.createDirectories(path.getParent());
        Files.write(path, image.getBytes());
        return filename;
    }
}

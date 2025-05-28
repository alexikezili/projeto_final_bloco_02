package com.generation.farmacia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.farmacia.model.Categoria;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.util.TestBuilder;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoriaControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	private static final String BASE_URL_CATEGORIA = "/categorias";
	
	@BeforeAll
	void start() {
		categoriaRepository.deleteAll();
	}
	
	@Test
	@DisplayName("Deve cadastrar uma categoria com sucesso")
	public void deveCadastrarCategoria() {
		//Given
		Categoria categoria = TestBuilder.criarCategoria(null, "Remédio");
		
		//When
		HttpEntity<Categoria> requisicao = new HttpEntity<>(categoria);
		ResponseEntity<Categoria> resposta = testRestTemplate.exchange(BASE_URL_CATEGORIA, HttpMethod.POST, requisicao, Categoria.class);
		
		//Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Remédio", resposta.getBody().getDescricao());
	}
	
	@Test
	@DisplayName("Deve atualizar uma categoria com sucesso")
	public void deveAtualizarUmaCategoria() {
		//Given
		Categoria categoria = TestBuilder.criarCategoria(null, "Cosmético");
		categoriaRepository.save(categoria);
		Categoria categoriaUpdate = TestBuilder.criarCategoria(categoria.getId(), "Bebida");
		
		//When
		HttpEntity<Categoria> requisicao = new HttpEntity<>(categoriaUpdate);
		ResponseEntity<Categoria> resposta = testRestTemplate.exchange(BASE_URL_CATEGORIA, HttpMethod.PUT, requisicao, Categoria.class);
		
		//Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals("Bebida", resposta.getBody().getDescricao());
	}
	
	@Test
	@DisplayName("Deve listar todas as categorias")
	public void deveListarTodasCategorias() {
		//Given
		categoriaRepository.save(TestBuilder.criarCategoria(null, "Remédio"));
		categoriaRepository.save(TestBuilder.criarCategoria(null, "Cosmético"));
		
		//When
		ResponseEntity<Categoria[]> resposta = testRestTemplate.exchange(BASE_URL_CATEGORIA, HttpMethod.GET, null, Categoria[].class);
		
		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertNotNull(resposta.getBody());
	}
	
	@Test
	@DisplayName("Deve buscar categoria por ID")
	public void deveBuscarCategoriaPorId(){
		//Given
		Categoria categoria = TestBuilder.criarCategoria(null, "Cosmético");
		categoriaRepository.save(categoria);
		
		//When
		ResponseEntity<Categoria> resposta = testRestTemplate.exchange(BASE_URL_CATEGORIA + "/" + categoria.getId(), HttpMethod.GET, null, Categoria.class);
				
		//Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals("Cosmético", resposta.getBody().getDescricao());
	}
}

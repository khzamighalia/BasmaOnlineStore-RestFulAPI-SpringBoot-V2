package com.BasmaOnlineStoreProduct.controller;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.BasmaOnlineStoreProduct.beans.Categorie;
import com.BasmaOnlineStoreProduct.beans.Produit;
import com.BasmaOnlineStoreProduct.repository.CategorieRepository;
import com.BasmaOnlineStoreProduct.repository.ProduitRepository;

@RepositoryRestController
public class Controller {

	@Autowired
	private CategorieRepository categorieRepository;

	@Autowired
	private ProduitRepository produitRepository;

	@ResponseBody
	@RequestMapping(path = "/categories", method = RequestMethod.POST)
	public ResponseEntity<Object> saveCategory(@RequestBody Categorie categorie) throws IOException {
		if(categorieRepository.findByName(categorie.getName())==null) {
		try {
			Image image = ImageIO.read(new URL(categorie.getImage()));
			if (image != null) {
				categorieRepository.save(categorie);
				return new ResponseEntity<>("L'ajout est terminé avec succés", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("L'URL ne contient aucune image", HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("L'URL invalide", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		} else {
			return new ResponseEntity<>("le nom de la  categorie existe déjà", HttpStatus.NOT_ACCEPTABLE);
		}
	}

	@ResponseBody
	@RequestMapping(path = "/produits", method = RequestMethod.POST)
	public ResponseEntity<Object> saveProduct(@RequestBody Produit produit) throws IOException {
		if(produitRepository.findByName(produit.getName()) == null) {
		if (produit.getImages().size() >= 4 && produit.getImages().size() <= 8) {
			for (String url : produit.getImages()) {
				try {
					Image image = ImageIO.read(new URL(url));
					if (image != null) {
					} else {
						return new ResponseEntity<>("L'URL ne contient aucune image : "+url, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (Exception e) {
					return new ResponseEntity<>("L'URL invalide : "+url, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			if(categorieRepository.existsById(produit.getCategorie().getId()) == true) {
				produit.setCategorie(categorieRepository.findById(produit.getCategorie().getId()).get());
				produitRepository.save(produit);
				return new ResponseEntity<>("L'ajout est terminé avec succés", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("L'id de la categorie est invalide", HttpStatus.NOT_FOUND);
			}
		} else {
			return new ResponseEntity<>("Le nombre des images doit être entre 4 et 8", HttpStatus.LENGTH_REQUIRED);
		}
		}else {
			return new ResponseEntity<>("le nom du produit existe déja", HttpStatus.NOT_ACCEPTABLE);
		}
		
	}

}

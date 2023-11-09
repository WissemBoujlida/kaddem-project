package tn.esprit.kaddemproject.generic;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


public class GenericController<T,ID> {

	@Autowired
	IGenericService<T,ID> genericService;


	@PostMapping()
	public T add(@RequestBody T entity) {
		return genericService.add(entity);
	}

	@PutMapping()
	public T update(@RequestBody T entity) {
		return	genericService.update(entity);
	}

	@GetMapping("/{id}")
	public T retrieveById(@PathVariable ID id ) {
		return genericService.retrieveById(id);
	}

	@GetMapping()
	public List<T> retrieveAll() {
		return	genericService.retrieveAll();
	}

	@DeleteMapping("/{id}")
	public Boolean delete(@PathVariable ID id) {
		return	genericService.delete(id);
	}
}

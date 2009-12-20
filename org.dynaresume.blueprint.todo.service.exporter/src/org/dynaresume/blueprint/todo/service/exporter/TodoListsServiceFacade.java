package org.dynaresume.blueprint.todo.service.exporter;

import java.util.Collection;

import org.dynaresume.blueprint.todo.domain.TodoList;
import org.dynaresume.blueprint.todo.service.ITodoListsService;

public class TodoListsServiceFacade implements ITodoListsService {
	private ITodoListsService concreteTodoListsService;
	
	@Override
	public void delete(TodoList list) {
		concreteTodoListsService.delete(list);		
	}

	@Override
	public TodoList getListbyId(Long id) {
		return concreteTodoListsService.getListbyId(id);
	}

	@Override
	public Collection<TodoList> getLists() {
		return concreteTodoListsService.getLists();
	}

	@Override
	public TodoList save(TodoList list) {
		return concreteTodoListsService.save(list);
	}

	public void setConcreteTodoListsService(
			ITodoListsService concreteTodoListsService) {
		this.concreteTodoListsService = concreteTodoListsService;
	}
}

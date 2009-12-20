package org.dynaresume.blueprint.todo.service;

import java.util.Collection;

import org.dynaresume.blueprint.todo.domain.TodoList;

public interface ITodoListsService {
	TodoList save(TodoList list);

	void delete(TodoList list);

	TodoList getListbyId(Long id);

	Collection<TodoList> getLists();
}

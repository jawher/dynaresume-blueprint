package org.dynaresume.blueprint.todo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.dynaresume.blueprint.todo.domain.TodoList;
import org.dynaresume.blueprint.todo.service.ITodoListsService;
import org.neo4j.api.core.NeoService;

public class TodoListsServiceImpl implements ITodoListsService {
	private Set<TodoList> db = new HashSet<TodoList>(10);
	private long idSeq = 1;

	@Override
	public void delete(TodoList list) {
		db.remove(db);
	}

	@Override
	public TodoList getListbyId(Long id) {
		for (TodoList list : db) {
			if (id.equals(list.getId())) {
				return list;
			}
		}
		throw new NoSuchElementException();
	}

	@Override
	public Collection<TodoList> getLists() {
		return new ArrayList<TodoList>(db);
	}

	@Override
	public TodoList save(TodoList list) {
		if (list.getId() == null) {
			list.setId(idSeq++);
		} else {
			db.remove(list);
		}
		db.add(list);
		return list;
	}

}

package org.dynaresume.blueprint.todo.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TodoList implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private List<TodoItem> items=new ArrayList<TodoItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TodoItem> getItems() {
		return items;
	}

	public void setItems(List<TodoItem> items) {
		this.items = items;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TodoList other = (TodoList) obj;
		return id.equals(other.id);
	}

	@Override
	public String toString() {
		return "TodoList [id=" + id + ", name=" + name + ", items=" + items
				+ "]";
	}
	
	
}

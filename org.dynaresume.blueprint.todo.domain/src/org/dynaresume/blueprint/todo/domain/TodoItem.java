package org.dynaresume.blueprint.todo.domain;

import java.io.Serializable;

public class TodoItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String description;
	private boolean done = false;
	private TodoList list;

	public TodoItem() {
	}

	public TodoItem(String description) {
		super();
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public TodoList getList() {
		return list;
	}

	public void setList(TodoList list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "TodoItem [description=" + description + ", done=" + done + "]";
	}

}

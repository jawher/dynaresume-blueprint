package org.dynaresume.blueprint.client.test;

import org.dynaresume.blueprint.todo.domain.TodoItem;
import org.dynaresume.blueprint.todo.domain.TodoList;
import org.dynaresume.blueprint.todo.service.ITodoListsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TodoTester {

	public static void main(String[] args) {
		ApplicationContext cx = new ClassPathXmlApplicationContext(
				"applicationContext.xml", TodoTester.class);
		ITodoListsService todoListsService = (ITodoListsService) cx.getBean(
				"todoListsService", ITodoListsService.class);
		TodoList list = new TodoList();
		list.setName("mos");
		list.getItems().add(new TodoItem("eat"));
		list.getItems().add(new TodoItem("watch"));
		list.getItems().add(new TodoItem("sleep"));
		//todoListsService.save(list);
		list= todoListsService.getListbyId(26L);
		todoListsService.save(list);
		System.out.println(list);
	}
}

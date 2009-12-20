package org.dynaresume.blueprint.todo.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.dynaresume.blueprint.todo.domain.TodoItem;
import org.dynaresume.blueprint.todo.domain.TodoList;
import org.dynaresume.blueprint.todo.service.ITodoListsService;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.NotFoundException;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;
import org.springframework.transaction.annotation.Transactional;

public class NeoTodoListsServiceImpl implements ITodoListsService {
	private static enum Relations implements RelationshipType {
		TODO_LISTS, TODO_LIST, ITEM_OF_LIST
	}

	private NeoService neoService;

	@Transactional
	protected Node getListsRefNode() {
		Iterator<Relationship> itr = neoService.getReferenceNode()
				.getRelationships(Relations.TODO_LISTS, Direction.OUTGOING)
				.iterator();
		if (itr.hasNext()) {
			return itr.next().getEndNode();
		} else {
			Node refNode = neoService.createNode();
			neoService.getReferenceNode().createRelationshipTo(refNode,
					Relations.TODO_LISTS);
			return refNode;
		}
	}

	@Transactional
	@Override
	public void delete(TodoList list) {
		Node listsRefNode = getListsRefNode();
		Iterator<Relationship> itr = listsRefNode.getRelationships(
				Relations.TODO_LIST, Direction.OUTGOING).iterator();
		while (itr.hasNext()) {
			Node n = itr.next().getEndNode();
			if (list.getId().equals(n.getId())) {
				n.delete();
			}
		}
	}

	private TodoList mapList(Node node) {
		TodoList list = new TodoList();
		list.setId(node.getId());
		list.setName((String) node.getProperty("name"));
		Iterator<Relationship> itr = node.getRelationships(
				Relations.ITEM_OF_LIST, Direction.INCOMING).iterator();
		list.setItems(new ArrayList<TodoItem>());
		while (itr.hasNext()) {
			Node itemNode = itr.next().getStartNode();
			TodoItem item = mapItem(itemNode);
			item.setList(list);
			list.getItems().add(item);
		}
		return list;
	}

	private void unmapList(TodoList list, Node node) {
		System.out.println("NeoTodoListsServiceImpl.unmapList(" +list+
				")");
		node.setProperty("name", list.getName());
		Iterator<Relationship> itr = node.getRelationships(
				Relations.ITEM_OF_LIST, Direction.INCOMING).iterator();
		while (itr.hasNext()) {
			itr.next().getStartNode().delete();
		}

		for (TodoItem item : list.getItems()) {
			Node itemNode = neoService.createNode();
			itemNode.createRelationshipTo(node, Relations.ITEM_OF_LIST);
			unmapItem(item, itemNode);
			System.out.println("save item "+itemNode);
		}
	}

	private TodoItem mapItem(Node node) {
		TodoItem item = new TodoItem();
		item.setDescription((String) node.getProperty("description"));
		item.setDone((Boolean) node.getProperty("done"));
		return item;
	}

	private void unmapItem(TodoItem item, Node node) {
		node.setProperty("description", item.getDescription());
		node.setProperty("done", item.isDone());
	}

	@Transactional
	@Override
	public TodoList getListbyId(Long id) {
		Node node;
		try {
			node = neoService.getNodeById(id);
		} catch (NotFoundException e) {
			throw new NoSuchElementException();
		}
		if (!node.hasRelationship(Relations.TODO_LIST, Direction.INCOMING)) {
			throw new IllegalArgumentException("Invalid list id");
		}
		return mapList(node);

	}

	@Transactional
	@Override
	public Collection<TodoList> getLists() {
		Iterator<Relationship> itr = getListsRefNode().getRelationships(
				Relations.TODO_LIST, Direction.OUTGOING).iterator();
		List<TodoList> lists = new ArrayList<TodoList>();
		while (itr.hasNext()) {
			lists.add(mapList(itr.next().getEndNode()));
		}
		return lists;
	}

	@Transactional
	@Override
	public TodoList save(TodoList list) {
		System.out.println("NeoTodoListsServiceImpl.save(" + list + ")");
		if (list.getId() == null) {
			Node node = neoService.createNode();
			getListsRefNode().createRelationshipTo(node, Relations.TODO_LIST);
			unmapList(list, node);
			list.setId(node.getId());
		} else {
			Node node = neoService.getNodeById(list.getId());
			if (!node.hasRelationship(Relations.TODO_LIST, Direction.INCOMING)) {
				throw new IllegalArgumentException("Invalid list id");
			}
			unmapList(list, node);
		}
		return list;
	}

	public void setNeoService(NeoService neoService) {
		this.neoService = neoService;
	}
}

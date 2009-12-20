package org.dynaresume.blueprint.security.domain;

import java.io.Serializable;

public class Grant implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String data;

	public Grant() {

	}

	public Grant(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public Grant withId(String id) {
		this.id = id;
		return this;
	}

	public Grant withData(String data) {
		this.data = data;
		return this;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return id + "=" + data;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || id == null || !(obj instanceof Grant)) {
			return false;
		}
		Grant g = (Grant) obj;
		if (g.id == null || !(g.id.equals(id))) {
			return false;
		}

		return (data == null && g.data == null)
				|| (data != null && g.data != null && data.equals(g.data));
	}

	@Override
	public int hashCode() {
		if (id == null)
			return 0;
		return (id + "(" + data + ")").hashCode();
	}

}

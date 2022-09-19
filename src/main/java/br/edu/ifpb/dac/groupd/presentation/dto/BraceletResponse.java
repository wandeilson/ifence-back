package br.edu.ifpb.dac.groupd.presentation.dto;

import java.util.List;
import java.util.Objects;

public class BraceletResponse {
	
	private Long id;
	private String name;
	private List<FenceResponseMin> fences;
	
	
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
	
	public List<FenceResponseMin> getFences() {
		return fences;
	}
	public void setFences(List<FenceResponseMin> fences) {
		this.fences = fences;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BraceletResponse other = (BraceletResponse) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}
	
	

}

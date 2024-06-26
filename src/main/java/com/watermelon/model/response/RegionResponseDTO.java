package com.watermelon.model.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegionResponseDTO {
	@JsonIgnore
	private Long id;
	private String region;
	@JsonProperty("locations")
	private List<String> listLocations;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public List<String> getListLocations() {
		return listLocations;
	}

	public void setListLocations(List<String> listLocations) {
		this.listLocations = listLocations;
	}
}

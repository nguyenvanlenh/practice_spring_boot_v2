package com.watermelon.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.model.response.EStatus;
import com.watermelon.model.response.LocationResponseDTO;
import com.watermelon.model.response.RegionResponseDTO;
import com.watermelon.model.response.ResponseData;
import com.watermelon.service.LocationService;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping("/location/{id}")
	public ResponseEntity<ResponseData> getLocationById(@PathVariable int id) {

		Optional<LocationResponseDTO> locationResponse = Optional.of(locationService.getLocationById(id));

		return locationResponse.isPresent()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseData(locationResponse, EStatus.SUCCESS.getStatus(),
								EStatus.SUCCESS.getTitle(), EStatus.SUCCESS.getDescription()))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData(null, EStatus.NOT_FOUND.getStatus(),
						EStatus.NOT_FOUND.getTitle(), EStatus.NOT_FOUND.getDescription()));

	}

	@GetMapping("/regions")
	public ResponseEntity<ResponseData> getAllRegions() {
		Optional<List<RegionResponseDTO>> regions = Optional.of(locationService.getAllRegions());
		return regions.isPresent()
				? ResponseEntity.status(HttpStatus.OK)
						.body(new ResponseData(regions, EStatus.SUCCESS.getStatus(), EStatus.SUCCESS.getTitle(),
								EStatus.SUCCESS.getDescription()))
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseData(null, EStatus.NOT_FOUND.getStatus(),
						EStatus.NOT_FOUND.getTitle(), EStatus.NOT_FOUND.getDescription()));
	}
}

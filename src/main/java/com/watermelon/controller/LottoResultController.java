package com.watermelon.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watermelon.model.entity.LocationResult;
import com.watermelon.model.entity.LottoResultEntity;
import com.watermelon.model.entity.PrizeResult;
import com.watermelon.repository.LottoResultRepository;

@RestController
@RequestMapping("/api/lottery")
@CrossOrigin("*")
public class LottoResultController {
	@Autowired
	private LottoResultRepository lottoResultRepository;
	
	
	
	@GetMapping("/MN")
	public List<LocationResult> getAllLoteryResult() {
		Map<String, LocationResult> locationResults = new HashMap<>();

		List<LottoResultEntity> lottoResultEntities = lottoResultRepository.findAll();

		for (LottoResultEntity entity : lottoResultEntities) {
		    String key = entity.getLocation();

		    if (!locationResults.containsKey(key)) {
		        LocationResult locationResult = new LocationResult(
		                entity.getId(),
		                entity.getLocation(),
		                entity.getWeekday(),
		                new SimpleDateFormat("dd-MM-yyyy").format(entity.getDate()),
		                entity.getRegion()
		        );
		        locationResults.put(key, locationResult);
		    }

		    LocationResult locationResult = locationResults.get(key);
		    List<String> result = Arrays.asList(entity.getResult().split("\\s+"))
		            .stream()
		            .filter(s -> s.matches("\\d+"))
//		            .map(Integer::parseInt)
		            .collect(Collectors.toList());




		    locationResult.addPrize(entity.getPrize(), result);
		}

		List<LocationResult> finalResult = new ArrayList<>(locationResults.values());

		// Sắp xếp theo giải
	    finalResult.forEach(locationResult -> {
	        locationResult.setPrizes(locationResult.getPrizes().stream()
	                .sorted(Comparator.comparing(PrizeResult::getPrizeName, 
	                                            (s1, s2) -> {
	                                                // Sắp xếp theo thứ tự giải từ Đặc biệt đến Giải tám
	                                                List<String> prizesOrder = List.of("Giải tám","Giải bảy" ,"Giải sáu" ,"Giải tư",
	                                                		"Giải năm" ,"Giải ba", "Giải nhì", "Giải nhất", "Đặc biệt");
	                                                return Integer.compare(prizesOrder.indexOf(s1), prizesOrder.indexOf(s2));
	                                            }))
	                .collect(Collectors.toList()));
	    });

	    return finalResult;
		
	}

}

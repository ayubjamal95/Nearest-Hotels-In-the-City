package com.booking.recruitment.hotel.service.impl;

import com.booking.recruitment.hotel.exception.BadRequestException;
import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.model.City;
import com.booking.recruitment.hotel.repository.CityRepository;
import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
class DefaultHotelService implements HotelService {
  private final HotelRepository hotelRepository;

  @Autowired
  DefaultHotelService(HotelRepository hotelRepository) {
    this.hotelRepository = hotelRepository;
  }

  @Autowired
  CityRepository cityRepository;
  
  @Override
  public List<Hotel> getAllHotels() {
    return hotelRepository.findAll();
  }

  @Override
  public List<Hotel> getHotelsByCity(Long cityId) {
    return hotelRepository.findAll().stream()
        .filter((hotel) -> cityId.equals(hotel.getCity().getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Hotel createNewHotel(Hotel hotel) {
    if (hotel.getId() != null) {
      throw new BadRequestException("The ID must not be provided when creating a new Hotel");
    }

    return hotelRepository.save(hotel);
  }
  
  @Override
  public Hotel getIndividualHotel(Long id) {
	  Hotel hotel = new Hotel();
	  if(id != null)
	  {
		 hotel = hotelRepository.findById(id).orElse(null);
	  }
	  return hotel;
  }
  
  @Override
  public Boolean deleteHotel(Long id) {
	  Hotel hotel = new Hotel();
	   hotel = hotelRepository.findById(id).orElse(null);
	   if(hotel != null)
	   {
		   hotel.setDeleted(true); 
		   return true;
	   }
	   else {
		   return false;
	   }
  }
  
  @Override
  public List<Hotel> searchNearestHotel(Long id, String distance)
  {
	  if (distance.equals("distance"))
	  {
		  City city = new City(); 
		  List<Hotel> topThree = new ArrayList();
		  LinkedHashMap<Double, Hotel> sortedMap = new LinkedHashMap<>();
		  city = cityRepository.findById(id).orElse(null); 
		  List<Hotel> hotels = hotelRepository.findAll();
		  for (int i=0; i < hotels.size(); i++)
		  {
			Double length =  haversine(city.getCityCentreLatitude(), city.getCityCentreLongitude(),
					  hotels.get(i).getLatitude(), hotels.get(i).getLongitude());
			sortedMap.put(length, hotels.get(i));
		  }
		  sortedMap.entrySet()
		    .stream()
		    .sorted(Map.Entry.comparingByKey())
		    .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
		 int i=0;
		 while( i < 3)
		 {
			  topThree.add(sortedMap.values().stream().iterator().next());
			  i++;
		 }
		  
	    return topThree;
	  }
	  return null;
  }
  
  

  static Double haversine(Double lat1, Double lon1,
		  Double lat2, Double lon2)
  {
      // distance between latitudes and longitudes
	  Double dLat = Math.toRadians(lat2 - lat1);
	  Double dLon = Math.toRadians(lon2 - lon1);

      // convert to radians
      lat1 = Math.toRadians(lat1);
      lat2 = Math.toRadians(lat2);

      // apply formulae
      Double a = Math.pow(Math.sin(dLat / 2), 2) +
                 Math.pow(Math.sin(dLon / 2), 2) *
                 Math.cos(lat1) *
                 Math.cos(lat2);
      Double rad = 6371.0;
      Double c = 2 * Math.asin(Math.sqrt(a));
      return rad * c;
  }

}

package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }
  
  
  @GetMapping("/{id}")
  ResponseEntity <Hotel> getIndividualHotel(@PathVariable("id") Long id) {
	  if(hotelService.getIndividualHotel(id) == null) {
		  return new ResponseEntity( HttpStatus.NOT_FOUND);
	  }
	  return new ResponseEntity( hotelService.getIndividualHotel(id), HttpStatus.OK);
  }
  
  @DeleteMapping("/{id}")
  ResponseEntity <Boolean> deleteHotel(@PathVariable("id") Long id) {
	  if(hotelService.deleteHotel(id) == false) {
		  return new ResponseEntity( HttpStatus.NOT_FOUND);
	  }
	  return new ResponseEntity( hotelService.deleteHotel(id), HttpStatus.OK);
  }
  
  @GetMapping("/search/{id}")
  ResponseEntity <Hotel> searchNearestHotel(@PathVariable("id") Long id, @RequestParam (value ="sort" , required = false) String distance)
  {
	  if ( hotelService.searchNearestHotel(id,distance).isEmpty() ) {
		  return new ResponseEntity( HttpStatus.NOT_FOUND);
	  }
	  return new ResponseEntity( hotelService.searchNearestHotel(id,distance), HttpStatus.OK);
  }
}

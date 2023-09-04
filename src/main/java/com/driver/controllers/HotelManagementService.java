package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;




@Service
public class HotelManagementService {


    HotelMagementRepository hotelManagementRepository =new HotelMagementRepository();

    public String addHotel(Hotel hotel){
        String ans = hotelManagementRepository.addHotel(hotel);
        return ans;
    }

    public Integer addUser(User user){
        int ans = hotelManagementRepository.addUser(user);
        return ans;
    }

    public String getHotelWithMostFacilities(){
        return hotelManagementRepository.getHotelWithMostFacilities();
    }

    public int bookARoom(Booking booking){
        return hotelManagementRepository.bookARoom(booking);
    }

    public int getBookings(Integer aadharCard){
        return hotelManagementRepository.getBookings(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        return hotelManagementRepository.updateFacilities(newFacilities, hotelName);
    }
}
package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Repository
public class HotelMagementRepository {

    HashMap<String, Hotel> hotelDb = new HashMap<>();
    HashMap<Integer, User> userDb = new HashMap<>();
    HashMap<String, Booking> bookingDb = new HashMap<>();


    public String addHotel(Hotel hotel){
        String key = hotel.getHotelName();

        if(key == null){
            return "FAILURE";
        }
        else if(hotelDb.containsKey(key)){
            return "FAILURE";
        }
        else{
            hotelDb.put(key, hotel);
        }
        return "SUCCESS";
    }

    public Integer addUser(@RequestBody User user){
        int key = user.getaadharCardNo();

        userDb.put(key, user);

        return key;
    }


    public String getHotelWithMostFacilities(){
        int noOfFacilities = 0;
        String ans = "";

        for(String hotelName : hotelDb.keySet()){
            Hotel hotel = hotelDb.get(hotelName);
            if(hotel.getFacilities().size() > noOfFacilities){
                ans = hotelName;
                noOfFacilities = hotel.getFacilities().size();
            }
            else if(hotel.getFacilities().size() == noOfFacilities){
                if(hotelName.compareTo(ans) < 0){
                    ans = hotelName;
                }
            }
        }
        return ans;
    }




    public Integer bookARoom(Booking booking){
        UUID uuid = UUID.randomUUID();
        String bookingId = uuid.toString();
        booking.setBookingId(bookingId);

        String hotelName = booking.getHotelName();
        Hotel hotel = hotelDb.get(hotelName);
        int pricePerNight = hotel.getPricePerNight();
        int noOfRooms = booking.getNoOfRooms();
        int availableRooms = hotel.getAvailableRooms();

        if(noOfRooms > availableRooms){
            return -1;
        }
        int amountPaid = noOfRooms * pricePerNight;
        booking.setAmountToBePaid(amountPaid);

        hotel.setAvailableRooms(availableRooms - noOfRooms);
        bookingDb.put(bookingId, booking);
        hotelDb.put(hotelName, hotel);

        return amountPaid;
    }

    public int getBookings(Integer aadharCard){
        int ans = 0;

        for(String bookingId : bookingDb.keySet()){
            Booking booking = bookingDb.get(bookingId);
            if(booking.getBookingAadharCard() == aadharCard){
                ans++;
            }
        }
        return ans;
    }


    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName){
        Hotel hotel = hotelDb.get(hotelName);
        List<Facility> currentFacilities = hotel.getFacilities();

        for(Facility facility : newFacilities){
            if(currentFacilities.contains(facility)){
                continue;
            }
            else{
                currentFacilities.add(facility);
            }
        }
        hotel.setFacilities(currentFacilities);
        hotelDb.put(hotelName, hotel);

        return hotel;
    }
}
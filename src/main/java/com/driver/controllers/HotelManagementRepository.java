package com.driver.controllers;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class HotelManagementRepository {
    Map<String, Hotel> hotelMap = new HashMap<>(); //hotelName -> hotel
    Map<Integer, User> userMap = new HashMap<>(); // adharNo -> user
    Map<String, Booking> bookingMap = new HashMap<>(); //BookingId -> booking
   // Map<String, Integer> userRent = new HashMap<>(); // BookingId -> amountToBePaid

    public String addHotel(Hotel hotel) {
        if (hotel.getHotelName() == null || hotel == null) return "FAILURE";
        if (hotelMap.containsKey(hotel.getHotelName())) return "FAILURE";
        String hotelName = hotel.getHotelName();
        hotelMap.put(hotelName, hotel);
        return "SUCCESS";
        //You need to add an hotel to the database
        //incase the hotelName is null or the hotel Object is null return an empty a FAILURE
        //Incase somebody is trying to add the duplicate hotelName return FAILURE
        //in all other cases return SUCCESS after successfully adding the hotel to the hotelDb.
    }

    public Integer addUser(User user) {
        //You need to add a User Object to the database
        //Assume that user will always be a valid user and return the aadharCardNo of the user
        int adharNum = user.getaadharCardNo();
        userMap.put(adharNum, user);
        return adharNum;
    }

    public String getHotelWithMostFacilities() {
        //Out of all the hotels we have added so far, we need to find the hotelName with most no of facilities
        //Incase there is a tie return the lexicographically smaller hotelName
        //Incase there is not even a single hotel with atleast 1 facility return "" (empty string)
        int maxFacility = 0;
        for (String key : hotelMap.keySet()) {
            List<Facility> facilities = hotelMap.get(key).getFacilities();
            maxFacility = Math.max(maxFacility, facilities.size());
        }

        if (maxFacility == 0) return "";
        List<String> hotelNames = new ArrayList<>();
        for (String key : hotelMap.keySet()) {
            List<Facility> facilities = hotelMap.get(key).getFacilities();
            if (facilities.size() == maxFacility) hotelNames.add(key);
        }
        Collections.sort(hotelNames);
        return hotelNames.get(0);
    }

    public int bookARoom(Booking booking) {

        //The booking object coming from postman will have all the attributes except bookingId and amountToBePaid;
        //Have bookingId as a random UUID generated String
        //save the booking Entity and keep the bookingId as a primary key
        //Calculate the total amount paid by the person based on no. of rooms booked and price of the room per night.
        //If there arent enough rooms available in the hotel that we are trying to book return -1
        //in other case return total amount paid

        String hotelName = booking.getHotelName();
        if (!hotelMap.containsKey(hotelName)) return -1;
        if (hotelMap.get(hotelName).getAvailableRooms() >= booking.getNoOfRooms()) {
            Hotel hotel = hotelMap.get(hotelName);
            int totalRoomAvailable = hotel.getAvailableRooms();
            totalRoomAvailable -= booking.getNoOfRooms();
            hotel.setAvailableRooms(totalRoomAvailable);
            hotelMap.put(hotelName, hotel);

            String bookingId = UUID.randomUUID() + "";
         //   System.out.println(bookingId + "bookingId");

            int amountTobePaid = hotel.getPricePerNight() * booking.getNoOfRooms();
            bookingMap.put(bookingId, booking);

           // userRent.put(bookingId, amountTobePaid);

          //  System.out.println(amountTobePaid + "Amount To Paid");
            return amountTobePaid;
        }
        return -1;
    }

    public int getBookings(Integer aadharCard) {
        int cnt = 0;
        for (String key : bookingMap.keySet()) {
            if (aadharCard.equals(bookingMap.get(key).getBookingAadharCard())) cnt++;
        }
        return cnt;
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {

        //We are having a new facilites that a hotel is planning to bring.
        //If the hotel is already having that facility ignore that facility otherwise add that facility in the hotelDb
        //return the final updated List of facilities and also update that in your hotelDb
        //Note that newFacilities can also have duplicate facilities possible

        if (!hotelMap.containsKey(hotelName)) return null;
        Hotel hotel = hotelMap.get(hotelName);
        List<Facility> facilities = hotel.getFacilities();
        for (int i = 0; i < newFacilities.size(); i++) {
            if (!facilities.contains(newFacilities.get(i))) facilities.add(newFacilities.get(i));
        }
        hotelMap.put(hotelName, hotel);
        return hotel;
    }
}
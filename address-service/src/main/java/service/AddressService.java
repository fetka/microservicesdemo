package service;

import entity.Address;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.AddressRepo;
import response.AddressResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private ModelMapper mapper;

    public AddressResponse findAddressByEmployeeId(int employeeId) {
        Optional<Address> addressByEmployeeId = addressRepo.findAddressByEmployeeId(employeeId);
        AddressResponse addressResponse = mapper.map(addressByEmployeeId, AddressResponse.class);
        return addressResponse;
    }

    public List<AddressResponse> getAddresses() {
        List<Address> addresses = addressRepo.findAll();
        List<AddressResponse> addressResponseList = new ArrayList<>();
        for (Address a : addresses) {
            addressResponseList.add(mapper.map(a, AddressResponse.class));
        }
        return addressResponseList;

    }
}
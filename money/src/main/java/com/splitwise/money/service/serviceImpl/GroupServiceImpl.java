package com.splitwise.money.service.serviceImpl;

import com.splitwise.money.entity.Group;
import com.splitwise.money.entity.User;
import com.splitwise.money.exception.AlreadyExistsException;
import com.splitwise.money.repository.GroupRepository;
import com.splitwise.money.repository.UserRepository;
import com.splitwise.money.service.GroupService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Log4j2
public class GroupServiceImpl implements GroupService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group addUserToGroup(Group group , Long userId){
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                if (group.getUsersInGroup().contains(user.get())) {
                    log.warn("User Already in the group !!");
                }
                group.getUsersInGroup().add(user.get());
                groupRepository.save(group);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public String removeUserFromGroup(Long userId ,Group group) {
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isPresent()) {
                if (group.getUsersInGroup().contains(user.get()))
                    throw new AlreadyExistsException("User Already in the group !!");
                group.getUsersInGroup().remove(user.get());
                return "Successfully Removed User : " + user.get().getUserName() + " from the group!!";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Could Not Remove the User with userId : " + userId;
    }

    @Override
    public String deleteGroup(Long groupId, User user) {
        try{
            Optional<Group> group = groupRepository.findById(groupId);
            if (group.isEmpty())
                return "Group Does Not Exist with the Group Id : " + groupId;
            groupRepository.deleteById(groupId);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "Successfully Deleted Group with groupId : ";
    }

    @Override
    public void save(Group group){
        groupRepository.save(group);
    }

    @Override
    public Boolean groupExistsById(Long groupId) {
        return groupRepository.existsById(groupId);
    }

    @Override
    public Optional<Group> findGroupById(Long groupId) {
        return groupRepository.findById(groupId);
    }
}

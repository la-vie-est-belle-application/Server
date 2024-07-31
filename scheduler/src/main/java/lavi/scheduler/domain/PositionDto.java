package lavi.scheduler.domain;

import lombok.Data;

import java.util.List;

    @Data
    public class PositionDto {

        private List<User> leader;
        private List<User> scan;
        private List<User> main;
        private List<User> dress;
        private List<User> assistant;
        private List<User> waitingRoom;
        private List<User> manager;
        private List<User> navigator;
        private List<User> dressRoom;
    }

package lavi.scheduler.domain;

import java.util.Arrays;
import java.util.List;

public enum Position {
    LEADER("팀장"),
    SCAN("스캔"),
    MAIN("메인"),
    DRESS("드레스"),
    ASSISTANT("축가"),
    WAITINGROOM("대기실"),
    MANAGER("매니저"),
    NAVIGATOR("안내"),
    DRESSROOM("드레스실")
    ;

    private final String positionName;

    Position(String positionName) {
        this.positionName = positionName;
    }

    public String getKorean() {
        return positionName;
    }

    //모든 포지션 정보 가져올 떄 사용
    public List<String> getAllPositions() {
        return Arrays.stream(Position.values())
                .map(Position::getKorean)
                .toList();
    }
}


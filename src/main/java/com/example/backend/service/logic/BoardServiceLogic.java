package com.example.backend.service.logic;

import com.example.backend.dto.BoardDTO;
import com.example.backend.entity.Board;
import com.example.backend.entity.Club;
import com.example.backend.entity.Membership;
import com.example.backend.entity.Role;
import com.example.backend.service.BoardService;
import com.example.backend.store.BoardStore;
import com.example.backend.store.ClubStore;
import com.example.backend.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceLogic implements BoardService {
    private final ClubStore clubStore;
    private final BoardStore boardStore;

    @Override
    @Transactional
    public void register(Long clubId, BoardDTO boardDTO, Long currentUserId) {
        Club club = clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));
        if (club.getBoardCount() > 5) {
            throw new BoardLimitOverException("Max board count is 5.");
        }

        Optional.ofNullable(boardStore.findByClub_ClubIdAndBoardTitle(clubId, boardDTO.getBoardTitle()))
                .ifPresent(board -> {throw new BoardDuplicationException("Same board title already exists.");});

        Role currentUserRole = getCurrentUserRoleInClub(clubId, currentUserId);
        if (currentUserRole != Role.PRESIDENT) {
            throw new NoPermissionToCreateBoard("Only the president can create board.");
        }

        Board board = boardDTO.DTOToEntity();
        board.setClub(club);
        boardStore.save(board);

        club.setBoardCount(club.getBoardCount() + 1);
        clubStore.save(club);
    }

    @Override
    public BoardDTO findBoard(Long boardId) {
        return boardStore.findById(boardId)
                .map(board -> new BoardDTO(board))
                .orElseThrow(() -> new NoSuchBoardException("No such board with id : " + boardId));
    }

    @Override
    public BoardDTO findByClubIdAndBoardTitle(Long clubId, String boardTitle) {
        return Optional.ofNullable(boardStore.findByClub_ClubIdAndBoardTitle(clubId, boardTitle))
                .map(board -> new BoardDTO(board))
                .orElseThrow(() -> new NoSuchBoardException("No such board with title : " + boardTitle));
    }

    @Override
    public Page<BoardDTO> findByClubId(Long clubId, Pageable pageable) {
        Page<Board> boards = Optional.ofNullable(boardStore.findByClub_ClubId(clubId, pageable))
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));

        return boards.map(Board::EntityToDTO);
    }

    @Override
    @Transactional
    public BoardDTO modify(Long clubId, BoardDTO boardDTO, Long currentUserId) {
        clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));

        Role currentUserRole = getCurrentUserRoleInClub(clubId, currentUserId);
        if (currentUserRole != Role.PRESIDENT) {
            throw new NoPermissionToModifyBoard("Only the president can modify board.");
        }

        Board targetBoard = boardStore.findById(boardDTO.getBoardId())
                .orElseThrow(() -> new NoSuchBoardException("No such board in the club."));

        targetBoard.setBoardTitle(boardDTO.getBoardTitle());

        return new BoardDTO(targetBoard);
    }

    @Override
    @Transactional
    public void remove(Long boardId, Long currentUserId) {
        Board board = boardStore.findById(boardId)
                .orElseThrow(() -> new NoSuchBoardException("No such board in the club."));
        Club club = board.getClub();

        Role currentUserRole = getCurrentUserRoleInClub(board.getClub().getClubId(), currentUserId);
        if (currentUserRole != Role.PRESIDENT) {
            throw new NoPermisiionToDeleteBoard("Only the president can delete board.");
        }

        boardStore.delete(board);

        club.setBoardCount(club.getBoardCount() - 1);
        clubStore.save(club);
    }

    private Role getCurrentUserRoleInClub(Long clubId, Long currentUserId) {
        Club club = clubStore.findById(clubId)
                .orElseThrow(() -> new NoSuchClubException("No such club with id : " + clubId));

        return club.getMemberships().stream()
                .filter(membership -> membership.getMember().getMemberId() == currentUserId)
                .findFirst()
                .map(Membership::getRole)
                .orElseThrow(() -> new NotInClubException("Current User is not in this club."));
    }
}

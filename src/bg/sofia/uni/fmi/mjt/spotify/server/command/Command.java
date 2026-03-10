package bg.sofia.uni.fmi.mjt.spotify.server.command;

import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.UserDoesNotExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.authentication.WrongPasswordException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.network.MissingClientChannelException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.NoSuchPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.PlaylistAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.playlist.SongAlreadyInPlaylistException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.AlreadyStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSongStreamingException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.song.NoSuchSongException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadEmailFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPasswordFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadPlaylistNameFormatException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSearchKeywordsException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadSongTitleException;
import bg.sofia.uni.fmi.mjt.spotify.common.exception.validation.BadTopNException;
import bg.sofia.uni.fmi.mjt.spotify.server.context.UserContext;
import bg.sofia.uni.fmi.mjt.spotify.server.reponse.CommandResponse;
import bg.sofia.uni.fmi.mjt.spotify.server.request.CommandRequest;

public interface Command {
    CommandResponse execute(CommandRequest request, UserContext userContext) throws BadEmailFormatException,
            UserDoesNotExistsException, BadPasswordFormatException, WrongPasswordException,
            UserAlreadyRegisteredException, BadSearchKeywordsException, BadPlaylistNameFormatException,
            PlaylistAlreadyExistsException, BadSongTitleException, NoSuchSongException, NoSuchPlaylistException,
            SongAlreadyInPlaylistException, MissingClientChannelException, NoSongStreamingException,
            AlreadyStreamingException, BadTopNException;
}

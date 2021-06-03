package scoreservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.helsinki.cs.tmc.edutestutils.Points;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class ScoreServiceRestApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    @Test
    @Points("06-07.1")
    public void testPostGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
    }

    @Test
    @Points("06-07.1")
    public void testPostAndGetGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        getGame(randomGameName);
    }

    @Test
    @Points("06-07.1")
    public void testPostAndGetGameAndListGames() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        Game g = getGame(randomGameName);
        expectToFindGameFromList(g.getId(), randomGameName);
    }

    @Test
    @Points("06-07.1")
    public void testPostAndGetGameAndListGamesAndDeleteGame() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);
        Game g = getGame(randomGameName);

        expectToFindGameFromList(g.getId(), randomGameName);
        deleteGame(randomGameName);
        expectNotToFindGameFromList(g.getId());
    }

    @Test
    @Points("06-07.2")
    public void testPostGameAndACoupleOfScores() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        Game g = postGame(randomGameName);

        int[] points = new int[3];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Random().nextInt(100000);
        }
        postScoresWithRandomNickname(randomGameName, points);
    }

    @Test
    @Points("06-07.2")
    public void testPostGameAndACoupleOfScoresAndGetScores() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] points = new int[5];
        String[] nicknames = new String[5];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Random().nextInt(100000);
            nicknames[i] = "nick:" + UUID.randomUUID().toString();
        }

        long[] scoreIds = postScoresWithSpecifiedNickname(randomGameName, nicknames, points);

        for (int i = 0; i < points.length; i++) {
            getScore(randomGameName, scoreIds[i], nicknames[i], points[i]);
        }
    }

    @Test
    @Points("06-07.2")
    public void testPostGameAndACoupleOfScoresAndListScores() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] points = new int[5];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Random().nextInt(100000);
        }
        long[] scoreIds = postScoresWithRandomNickname(randomGameName, points);

        getScores(randomGameName);
        expectToFindScoreFromList(randomGameName, scoreIds, points);
    }

    @Test
    @Points("06-07.2")
    public void testPostGameAndACoupleOfScoresAndListScoresAndDeleteSpecificScores() throws Exception {
        String randomGameName = "game:" + UUID.randomUUID().toString();
        postGame(randomGameName);

        int[] points = new int[7];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Random().nextInt(100000);
        }
        long[] scoreIds = postScoresWithRandomNickname(randomGameName, points);

        getScores(randomGameName);
        expectToFindScoreFromList(randomGameName, scoreIds, points);

        deleteScore(randomGameName, scoreIds[0]);
        deleteScore(randomGameName, scoreIds[2]);
        deleteScore(randomGameName, scoreIds[5]);

        expectNotToFindScoreFromList(randomGameName, new long[]{scoreIds[0], scoreIds[2], scoreIds[5]});
    }

    public long[] postScoresWithSpecifiedNickname(String gameName, String[] nicknames, int[] points) throws Exception {
        long[] ids = new long[points.length];
        for (int i = 0; i < points.length; i++) {
            Score score = postScore(gameName, nicknames[i], points[i]);
            ids[i] = score.getId();
        }
        return ids;
    }

    public long[] postScoresWithRandomNickname(String gameName, int[] points) throws Exception {
        long[] ids = new long[points.length];
        for (int i = 0; i < points.length; i++) {
            String randomNickname = "nickname:" + UUID.randomUUID().toString();
            Score score = postScore(gameName, randomNickname, points[i]);
            ids[i] = score.getId();
        }
        return ids;
    }

    protected void expectToFindGameFromList(Long expectedId, String expectedName) throws Exception {
        List<Game> games = getGames();

        Game matchingGame = null;
        for (Game game : games) {
            if (expectedId.equals(game.getId())) {
                matchingGame = game;
                break;
            }
        }

        if (matchingGame == null) {
            fail("Cannot find a stored game in the response of GET-request to /games");
            return;
        }

        assertEquals("GET-request to /games returns incorrect name for game",
                expectedName, matchingGame.getName());
    }

    protected void expectToFindScoreFromList(String gameName, long[] expectedIds, int[] expectedPoints) throws Exception {
        List<Score> scores = getScores(gameName);

        int foundCount = 0;
        for (Score score : scores) {

            boolean foundId = false;
            for (int i = 0; i < expectedIds.length; i++) {
                long expectedId = expectedIds[i];
                int expectedPnts = expectedPoints[i];

                if (new Long(expectedId).equals(score.getId())) {
                    foundId = true;

                    assertEquals("GET-request to /games/" + gameName + "/scores returns incorrect number of points for a score",
                            new Long(expectedPnts), score.getPoints());
                    break;
                }
            }
            if (!foundId) {

                fail("Cannot find a stored score in the response of GET-request to /games/" + gameName + "/scores");
            } else {
                foundCount++;
            }
        }

        assertEquals("All scores should be returned by the getScores-method.", expectedPoints.length, foundCount);
    }

    protected void expectNotToFindGameFromList(Long expectedId) throws Exception {
        List<Game> games = getGames();

        Game matchingGame = null;

        for (Game game : games) {
            if (expectedId.equals(game.getId())) {
                matchingGame = game;
                break;
            }
        }

        if (matchingGame != null) {
            fail("Expected not to find a game after deletion in the response of GET-request to /games");
        }
    }

    protected void expectNotToFindScoreFromList(String gameName, long[] expectedIds) throws Exception {
        List<Score> scores = getScores(gameName);

        for (Score score : scores) {
            boolean foundId = false;

            for (int i = 0; i < expectedIds.length; i++) {
                long expectedId = expectedIds[i];

                if (new Long(expectedId).equals(score.getId())) {
                    foundId = true;
                    break;
                }
            }

            if (foundId) {
                fail("Expected not to find a score after deletion in the response of GET-request to /games/" + gameName + "/scores");
            }
        }
    }

    protected Game postGame(String name) throws Exception {
        Game game = gameRepository.findByName(name);
        assertNull("Game should not exist before creation; now game " + name + " was available in database.", game);

        Game g = new Game();
        g.setName(name);

        String gameJson = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).writeValueAsString(g);
        mockMvc.perform(
                post("/games").contentType(MediaType.APPLICATION_JSON).content(gameJson)
        ).andExpect(status().isOk());

        game = gameRepository.findByName(name);

        assertNotNull("Game should exist after creation; now game " + name + " was unavailable in database.", game);
        assertNotNull("Game should have an id, now created game had id " + game.getId(), game.getId());

        return game;
    }

    protected Game getGame(String name) throws Exception {

        MvcResult res = mockMvc.perform(get("/games/{name}", name)).andExpect(status().isOk()).andReturn();

        Game game = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), Game.class);

        assertNotNull("When retrieving a game through the /games/{name} -address, game was not found. Verify that the GET-request works.", game);
        assertNotNull("When retrieving a game through the /games/{name} -address, the retrieved game should have the database id. Now the id was " + game.getId(), game.getId());
        assertEquals("When retrieving a game through the /games/{name} -address with name \"" + name + "\", the retrieved game should have the name \"" + name + "\". Now the name was " + game.getName(), name, game.getName());

        return game;
    }

    protected Game deleteGame(String name) throws Exception {

        MvcResult res = mockMvc.perform(delete("/games/{name}", name)).andExpect(status().isOk()).andReturn();

        Game game = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), Game.class);

        assertNotNull("When deleting a game through /games/{name} -address, game was not found. Verify that the DELETE-method works.", game);
        assertNotNull("When deleting a game through /games/{name} -address, the retrieved game should have the database id of the old deleted game. Now the id was " + game.getId(), game.getId());
        assertEquals("When deleting a game through /games/{name} -address with name \"" + name + "\", the retrieved game should have the name of the old deleted game \"" + name + "\". Now the name was " + game.getName(), name, game.getName());

        Game deleted = gameRepository.findByName(name);
        assertNull("Game should not exist after deletion; now game " + name + " was available in database.", deleted);

        return deleted;
    }

    protected List<Game> getGames() throws Exception {
        MvcResult res = mockMvc.perform(get("/games")).andExpect(status().isOk()).andReturn();

        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), new TypeReference<List<Game>>() {
        });

    }

    protected Score postScore(String gameName, String nickname, int points) throws Exception {
        Score s = new Score();
        s.setNickname(nickname);
        s.setPoints(new Long(points));

        String json = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).writeValueAsString(s);

        MvcResult res = mockMvc.perform(
                post("/games/{name}/scores", gameName).contentType(MediaType.APPLICATION_JSON).content(json)
        ).andExpect(status().isOk()).andReturn();

        Score response = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), Score.class);

        assertNotNull("When posting a score to a game, the response should contain the game details. Now the response was null.", response);
        assertNotNull("When posting a score to a game, the response should contain a game that has an id. Now the game id was " + response.getId(), response.getId());

        assertEquals("When posting a score to a game, the response should the used nick. Now the nick was " + response.getNickname(), nickname, response.getNickname());
        assertEquals("When posting a score to a game, the response should the used points. Now the points were " + response.getPoints(), new Long(points), response.getPoints());

        return response;
    }

    protected Score getScore(String gameName, Long scoreId,
            String expectedNickname, int expectedPoints) throws Exception {

        MvcResult res = mockMvc.perform(
                get("/games/{name}/scores/{scoreId}", gameName, scoreId)
        ).andExpect(status().isOk()).andReturn();

        Score response = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), Score.class);

        assertEquals("Retrieving a score with id " + scoreId + ", invalid nickname received.", expectedNickname, response.getNickname());
        assertEquals("Retrieving a score with id " + scoreId + ", invalid points received.", new Long(expectedPoints), response.getPoints());

        return response;
    }

    protected Score deleteScore(String gameName, Long scoreId) throws Exception {
        MvcResult res = mockMvc.perform(
    
                delete("/games/{name}/scores/{scoreId}", gameName, scoreId)
        ).andExpect(status().isOk()).andReturn();

        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), Score.class);
    }

    protected List<Score> getScores(String gameName) throws Exception {
        MvcResult res = mockMvc.perform(get("/games/{name}/scores", gameName)).andExpect(status().isOk()).andReturn();

        return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(res.getResponse().getContentAsString(), new TypeReference<List<Score>>() {
        });

    }
}

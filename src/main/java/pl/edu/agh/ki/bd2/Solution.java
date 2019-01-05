package pl.edu.agh.ki.bd2;

public class Solution {

    private final GraphDatabase graphDatabase = GraphDatabase.createDatabase();

    public void databaseStatistics() {
       // System.out.println(graphDatabase.runCypher("CALL db.labels()"));
       // System.out.println(graphDatabase.runCypher("CALL db.relationshipTypes()"));
    }

    public void runAllTests() {
//        System.out.println(findActorByName("Bradley Cooper"));
//        System.out.println(findMovieByTitleLike("Sabrina"));
//        System.out.println(findRatedMoviesForUser("maheshksp"));
//        System.out.println(findCommonMoviesForActors("Keanu Reeves", "Laurence"));
//        System.out.println(findMovieRecommendationForUser("emileifrem"));
//        System.out.println(addActor("Bobo","21797129927","Krakow, Poland","hueuhehueuhuehu"));
//        System.out.println(addMovie("Buba"));
//        System.out.println(addacts_inrelationship("Bobo","Buba"));
//        System.out.println(setMovie("Buba"));
//        System.out.println(findActorswithmorethan6());
//        System.out.println(findAvgforActorswithmorethan7());
//        System.out.println(findActorsDirectorswithmorethan5());
//        System.out.println(findFriendsWithRatedMoreThan3Stars());
//        System.out.println(pathBetweenActorsWithoutMovies("Bradley Cooper","Amy Adams"));
          measureTime();


    }

    private String findActorByName(final String actorName) {
        return graphDatabase.runCypher(String.format (
                "MATCH (n:Actor {name: '%s'})  RETURN n" ,actorName)) ;
    }

    private String findMovieByTitleLike(final String movieName) {

        return graphDatabase.runCypher(String.format (
                "MATCH (n:Movie {title: '%s'}) RETURN n" ,movieName)) ;
    }

    private String findRatedMoviesForUser(final String userLogin) {

        return graphDatabase.runCypher(String.format (
                "MATCH p=(n:User)-[r:RATED]->() Where n.login contains '%s' RETURN p" ,userLogin)) ;
    }

    private String findCommonMoviesForActors(String actorOne, String actrorTwo) {

        return graphDatabase.runCypher(String.format (
                "MATCH (n:Actor)-[r:ACTS_IN]->(m)<-[p:ACTS_IN]-(k:Actor)\n" +
                        "        Where n.name contains '%s' AND k.name contains '%s' RETURN m.title" ,actorOne,actrorTwo)) ;
    }

    private String findMovieRecommendationForUser(final String userLogin) {
        return graphDatabase.runCypher(String.format (
                "        MATCH (d:Director)-[:DIRECTED]->(m)<-[:RATED]-(p:User),(d)-[:DIRECTED]->(other:Movie) " +
                        "Where p.login contains '%s' " +
                        "AND NOT (other.title in m.title) RETURN distinct other.title" ,userLogin)) ;
    }

    private String addActor(String name, String birthday, String birthplace, String biography){
        String query = String.format("CREATE (p:Actor{name:'%s',birthday: '%s',birthplace: '%s',biography:'%s' })",
                name, birthday,birthplace,biography);
        return graphDatabase.runCypher(String.format(query));
    }
    private String addMovie(String title){
        String query = String.format("CREATE (p:Movie{title: '%s' })", title);
        return graphDatabase.runCypher(String.format(query));
    }

    private String addacts_inrelationship(String actor, String movie){
        String query = String.format("MATCH (p:Actor{name:'%s'}), (a:Movie{title:'%s'}) "
                + "CREATE (p)-[:ACTS_IN]->(a)", actor, movie);
        return graphDatabase.runCypher(String.format(query));
    }
    private String setMovie(String title){
        String query = String.format("MATCH (a:Movie{title:'%s'}) SET a.language = 'en'",title);
        return graphDatabase.runCypher(String.format(query));
    }
    private String findActorswithmorethan6() {
        return graphDatabase.runCypher(String.format (
                "Match (n:Actor)-[:ACTS_IN]->(m) With n, collect(n) as p Where length(p)>=6 return n.name"));
    }
    private String findAvgforActorswithmorethan7() {
        return graphDatabase.runCypher(String.format (
                " Match (n:Actor)-[:ACTS_IN]->(m) With n, collect(n) as p Where length(p)>=7 return avg(length(p))"));
    }
    private String findActorsDirectorswithmorethan5() {
        return graphDatabase.runCypher(String.format (
                "Match (n:Actor)-[:ACTS_IN]->(m) With n, collect(n) as p Where length(p)>=5 and " +
                        "(n)-[:DIRECTED]->() return n.name order by length(p)"));
    }
    private String findFriendsWithRatedMoreThan3Stars() {
        return graphDatabase.runCypher(String.format (
                "    MATCH (p:User)-[f:FRIEND]->(l)-[r:RATED]->(m) Where p.login=\"adilfulara\" " +
                        "AND r.stars>=3 RETURN l.login,r.stars,m.title\n"));
    }
    private String pathBetweenActorsWithoutMovies(String actor1, String actor2) {
        return graphDatabase.runCypher(String.format (
                "MATCH e=shortestPath((p:Actor)-[*]-(r:Actor)) Where p.name contains '%s' " +
                        "" +"AND r.name contains '%s' RETURN filter(x IN nodes(e) Where x:Person)",actor1,actor2));
    }
    private String createIndex(){
        String query = String.format("CREATE INDEX ON :Actor(name)");
        return graphDatabase.runCypher(String.format(query));
    }
    private String dropIndex(){
        String query = String.format("DROP INDEX ON :Actor(name)");
        return graphDatabase.runCypher(String.format(query));
    }
    private String findActorByNameExplain(final String actorName) {
        return graphDatabase.runCypher(String.format (
                "PROFILE MATCH (n:Actor {name: '%s'})  RETURN n" ,actorName)) ;
    }
    private String shortestpathBetweenActors(String actor1, String actor2) {
        return graphDatabase.runCypher(String.format (
                "PROFILE MATCH e=shortestPath((p:Actor)-[*]-(r:Actor)) Where p.name contains '%s' " +
                        "AND r.name contains '%s' RETURN e",actor1,actor2));
    }

    private void measureTime(){

        long startTime = System.nanoTime();

        System.out.println(findActorByNameExplain("Bradley Cooper"));

        long estimatedTime = System.nanoTime() - startTime;
        System.out.println("Czas dla findActorByName: "+estimatedTime);

        startTime = System.nanoTime();
        System.out.println(shortestpathBetweenActors("Bradley Cooper","Amy Adams"));
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Czas dla shortestpathBetweenActors: "+estimatedTime);

        createIndex();
        startTime = System.nanoTime();
        System.out.println( findActorByNameExplain("Bradley Cooper"));
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Czas dla findActorByName z index: "+estimatedTime);

        startTime = System.nanoTime();
        shortestpathBetweenActors("Bradley Cooper","Amy Adams");
        System.out.println(  shortestpathBetweenActors("Bradley Cooper","Amy Adams"));
        estimatedTime = System.nanoTime() - startTime;
        System.out.println("Czas dla shortestpathBetweenActors z index: "+estimatedTime);
        dropIndex();
    }

}

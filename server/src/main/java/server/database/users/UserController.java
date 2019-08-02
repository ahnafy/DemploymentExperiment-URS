package server.database.users;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Map;

public class UserController {

    private final MongoCollection<Document> userCollection;

    public UserController(MongoDatabase database) {
        userCollection = database.getCollection("users");
    }

    /**
     * Helper method that gets a single user specified by the `subjectID`
     * //     * parameter in the request.
     * //     *
     * //     * @param subjectID the Google subjectID of the desired user
     * //     * @return the desired user as a JSON object if the user with that subjectID is found,
     * //     * and `null` if no user with that subjectID is found
     * //
     */
    public String getUserBySub(String subjectID) {
        Document filterDoc = new Document();
        filterDoc.append("SubjectID", subjectID);

        FindIterable<Document> user = userCollection.find(filterDoc);

        return JSON.serialize(user);
    }

    /**
     * Helper method which iterates through the collection, receiving all
     * //     * documents if no query parameter is specified. If the SubjectID query parameter
     * //     * is specified, then the collection is filtered so only documents of that
     * //     * specified SubjectID are found.
     * //     *
     * //     * @param queryParams
     * //     * @return an array of Users in a JSON formatted string
     * //
     */
    public String getUsers(Map<String, String[]> queryParams) {

        Document filterDoc = new Document();

        if (queryParams.containsKey("SubjectID")) {
            int targetAge = Integer.parseInt(queryParams.get("SubjectID")[0]);
            filterDoc = filterDoc.append("SubjectID", targetAge);
        }

        if (queryParams.containsKey("FirstName")) {
            String targetContent = (queryParams.get("FirstName")[0]);
            Document contentRegQuery = new Document();
            contentRegQuery.append("$regex", targetContent);
            contentRegQuery.append("$options", "i");
            filterDoc = filterDoc.append("company", contentRegQuery);
        }

        FindIterable<Document> matchingUsers = userCollection.find(filterDoc);

        return JSON.serialize(matchingUsers);
    }

    public String addNewUser(String subjectID, String firstName, String lastName) {

        Document newUser = new Document();
        newUser.append("SubjectID", subjectID);
        newUser.append("FirstName", firstName);
        newUser.append("LastName", lastName);
        newUser.append("ShirtSize", "");
        newUser.append("Role", "user");

        try {
            userCollection.insertOne(newUser);
            ObjectId id = newUser.getObjectId("_id");
            System.err.println("Successfully added new user [_id= " + id + ", SubjectID= " + subjectID + ", FirstName= " + firstName + ", LastName= " + lastName + "]");

            return JSON.serialize(id);
        } catch (MongoException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String editUsertShirtSize(String userID, String size) {

        Document newtShirtSize = new Document();
        newtShirtSize.append("ShirtSize", size);

        return updateUserField(size, userID, newtShirtSize);
    }

    public String editUserrole(String userID, String position) {

        Document newrole = new Document();
        newrole.append("role", position);

        return updateUserField(position, userID, newrole);
    }

    private String updateUserField(String field, String id, Document updateDoc) {
        Document setQuery = new Document();
        Document searchQuery = new Document();

        setQuery.append("$set", updateDoc);

        try {
            searchQuery.append("_id", new ObjectId(id));
            userCollection.updateOne(searchQuery, setQuery);

            return JSON.serialize(field);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

            return new Document().toJson();
        } catch (MongoException e) {
            e.printStackTrace();

            return null;
        }
    }

}

# WebServicesFramework Documentation
GET requests
The requests that come as get request are classified as:-
1. The request with no query string. (/service/student/kite). The response contains the String which the particular service method returns.
2. The request with query string. (/service/student/details?rr=101&nn=Sameer). The response contains the String which the particular service method returns.
3. The request in which some object is kept in the request scope. The object is fetched from the request scope and converted into the object of the class which is the value of the parameter annotation.
POST requests
The request in which JSON String come. The JSON String is converted into the Object which is passed as an argument to the service method.

Types of Annotation:
Path - This can only be applied on class and methods. The value of this annotation is used to find the url pattern to call the service method.
Forward - The value of this annotation can be another url pattern or jsp for which request needs to be forwarded.
ClassType - This annotation is used when the method recieves only single parameter and only the object on any class.
ResponseType - The value of this annotation can only be - JSON,HTML and Nothing. Based on the value of this annotation, necessary String is returned
RequestData - This annotation is applied on methods when the parameters it recieves will come from query string.

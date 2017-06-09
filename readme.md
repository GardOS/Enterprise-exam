#### Enterprise-exam

The following structure is similar to jsf/jacoco and earlier exams (see ref.).
Therefore the location of the packages should not need further description than the model.

ref: https://github.com/arcuri82/testing_security_development_enterprise_systems

#### Structure
Note: skipping some layers for readability

- backend     
    - main     
        - ejb
        - entity
        - persistence.xml            
    - test     
        - ejb
        - util
        - arquillian.xml        
- frontend     
    - main     
        - java(controllers)
        - webapp
            - web-inf
    - test     
        - po(pageObjects)
- report

#### Code:
All exercises were attempted.

Methods/tests specifically requested in the assignment are at the top in the order they were requested.

Classes are segmented in to "requested" and "own" sections for easier reading. 
Code past the "own"-comment are helper methods or my own tests. 
Classes without specifically requested methods/tests(like .xhtml and controllers) does not have this comment.

##### Entity:
There is a ManyToMany-relation between Dish and Menu with eager loading. 
It is a bit more expensive but allows for more flexibility with queries/persisting/removing/merging 
(the scale of the DB is small anyway).

"Add reasonable constraints"

Dish: There are only constraints on length so that it cant take up too much of the screen. 
There are no more constraints (such as unique) because the users should be free to create what they want
(they can always delete it later).

Menu: Unique data and not-empty dishlist as requested. 
Left out date-formatting since i wanted to use LocalDate and regex comes with lots of implications, 
so i handled the format in a higher layer.

#### Added functionality:
- User-entity/UserEJB. Tested in backend/test/MenuEJBTest.
- Create user. Tested in frontend/test/MyCantinaIT.
    - Tests: testCreateValidUser, testCreateUserFailDueToPasswordMismatch.
- Login-functionality based on User. Tested in frontend/test/MyCantinaIT.
    - Tests: testLoginLink, testLoginWrongUser, testLogin.
- Permissions on create dish. Tested in frontend/test/MyCantinaIT.
    - Test: onlyUsersCanCreateDish.
- Remove dishes (not if inside menu). Tested in frontend/test/MyCantinaIT.
    - Test: testRemoveDish, testCantRemoveDishInMenu.
- Remains on page when submitting invalid dish/menu. NOT tested.
- Permissions on create menu. NOT tested.
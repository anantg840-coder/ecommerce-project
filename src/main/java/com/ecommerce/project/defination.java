package com.ecommerce.project;

public class defination {
    // request body
    // it is the data inside the package when the user sends the data in json format
    // it convert it into normal text format and send it into category;
    // path variable Data written in the address

      // custom exceptions-makes easier error handling
    // DTO's data transfer object
    // separation of concerns
    // you can hide any information /custoum response/pagination
    // done by dto without changing data model of our application
//      DTO (Data Transfer Object) is a simple Java class used to transfer
//       only the required data between the client and the server. It helps
//      hide unnecessary information and makes the API safer and cleaner.

//    model mapper
//
//    Client
//   │
//           ▼
//    JSON
//   │
//           ▼
//    Controller
//   │
//           ▼
//    CategoryDto
//   │
//           │  ModelMapper
//   ▼
//    Category (Entity)
//   │
//           ▼
//    Repository
//   │
//           ▼
//    Database
//
//    When data comes back:
//
//    Database
//   │
//           ▼
//    Repository
//   │
//           ▼
//    Category (Entity)
//   │
//           │  ModelMapper
//   ▼
//    CategoryDto
//   │
//           ▼
//    Controller
//   │
//           ▼
//    JSON Response
//   │
//           ▼
//    Client
//
//
//

    // @Request param is used to so the spring boot readsthe request paramaeters
    //it is genrally used in sorting,pagination

//    JPA is the standard API (rules) for mapping Java objects to database tables.
//    Hibernate is the implementation that follows those rules and generates SQL for you.
//    Relationships describe how tables are connected:
//    @OneToOne → one object linked to one object.
//    @OneToMany → one parent linked to many children.
//    @ManyToOne → many children linked to one parent.
//    @ManyToMany → many objects linked to many objects through a join table.

    //in bidirectional mapping both classes must aware of the
    // both the classes that contain the relationship





    //cascading
//    When two entities are associated (for example, Category and Product), you can tell JPA:
//
//            “If I perform an operation on the parent entity, automatically perform the same operation on the related child entities.”
//
//
//
//                                     ┌───────────────┐
//                                             │    CLIENT     │
//            └───────┬───────┘
//            │
//            │ POST /signin
//                            │ username + password
//                            ▼
//                                    ┌──────────────────────┐
//                                    │   Security Filter    │
//            │       Chain          │
//            └──────────┬───────────┘
//            │
//            ▼
//    Signin API
//                            │
//                                    ▼
//                                    ┌──────────────────────┐
//                                    │ AuthenticationManager│
//            └──────────┬───────────┘
//            │
//            ▼
//            ┌──────────────────────┐
//            │ DaoAuthentication    │
//            │ Provider             │
//            └──────────┬───────────┘
//            │
//            ┌───────────┴───────────┐
//            │                       │
//            ▼                       ▼
//    UserDetailsService         PasswordEncoder
//                │                       │
//                        ▼                       │
//    UserRepository                 │
//            │                       │
//            ▼                       │
//    DATABASE                    │
//            │                       │
//            ▼                       │
//    UserDetailsImpl                │
//            │                       │
//            └───────────┬───────────┘
//            │
//            ▼
//    Password Match
//                            │
//                                    ▼
//    AUTHENTICATED
//                            │
//                                    ▼
//    Generate JWT
//                            │
//                                    ▼
//    Send JWT
//                            │
//                                    ▼
//    CLIENT


//    Browser
//   │
//           │ Cookie: JSESSIONID=ABC123
//   ▼
//    Spring Security Filter Chain
//   │
//           ▼
//    Session Management
//   │
//           ▼
//    Find session ABC123
//   │
//           ▼
//    Retrieve Authentication
//   │
//           ▼
//    SecurityContext
//   │
//           ▼
//           "User is authenticated" ✅
//           │
//           ▼
//    Controller


}

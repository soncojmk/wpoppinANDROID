# wpoppinANDROID
Android app for What'sPoppin


/*Copyright (c) 2017 What'sPoppin LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 
 If given access, redistribution of this code is not allowed and changes made belong to WhatsPoppin LLC.
 */

```
API Endpoints
    My Profile Page:
        api/myrecommended --> Recommended people to follow (need to send token with request)
        api/myaccount --> The requesting users account info: email, token ... (need to send token with request)
        api/account/<user_id>/saved --> User's saved events based on their url
        api/account/<user_id>/posted --> User's posted events based on their url
        api/account/<user_id>/followers --> Get a user's followers list
        api/account/<user_id>/following --> Get a user's following list 
        api/account/<user_id>/requesting --> Get a list of users that are requesting to follow the current user (need to send token)
        api/account/<user_id>/requested --> Get a list of users the current user has requested to follow (need to send token with request)
    
    Following a user:
        api/account/<user_id>/follow  (POST) --> Follow a user (need to send token with request)
        api/account/<user_id>/follow (PUT) --> Accept a follow request (need to send token with request)
        api/account/<user_id>/follow (DELETE) --> Delete a follower or a follow request (need to send token with request)
    
    Event Card:
        api/events/<event_id>/save (POST) --> Save an event (need to send token with request)
        api/events/<event_id>/save (DELETE) --> Unsave an event (need to send token with request)
        api/events/<event_id>/people_saving (GET) --> get a list of users saving an event
        
    Notifications:
        api/devices --> Add a user's registration_id to our database. Link a user to a device 
                    --> (need to send token with request)
                    -->  Needed in order to allow device specific notifications on events like following, commenting...
        
All other API endpoints that don't have any internal/hidden endpoints and are fairly straight forward to use are in the link below
--> http://wpoppin.com/api/   (the links are pretty self explanatory0
```

# API Reference

This service exposes a rest API with the following endpoints

## `GET /api/submissions`
### Query params
All query params are optional
 - `limit` How many submissions to return. Cannot be negative, and maximum limit allowed is 200. Default is 10
 - `offset` How many submissions to skip. Cannot be negative. Default is 0
 - `sorting_field` Results will be sorted on this field. 
    Possible values: `account_name` `uw_name` `premium_usd` `effective_date` `expiration_date`
    Default is `account_name`
 - `sorting_direction` Wheter to return the results in ascending or descending order.
   Possible values: `asc` `desc`
   Default is `asc`

### Responses
- Returns a `status 200` and a JSON with the fetched submissions
- Returns a `status 400` when the `limit` or `offset` is negative or not a number

### Examples
```bash
curl -X 'GET' \
  'http://localhost:8080/api/submissions?limit=2&offset=100&sort_field=premium_usd&sort_direction=desc' 
```
```json
[ {
  "lines_of_coverage" : [ "Coverage2" ],
  "premium_usd" : 533.00,
  "state" : "NC",
  "account_name" : "Account10954490",
  "status" : "New",
  "effective_date" : "11/03/2024",
  "sic" : "SIC868",
  "expiration_date" : "16/12/2025",
  "uw_name" : "UW5"
}, {
  "lines_of_coverage" : [ "Coverage5" ],
  "premium_usd" : 1629.00,
  "state" : "VA",
  "account_name" : "Account10979053",
  "status" : "In Progress",
  "effective_date" : "28/02/2024",
  "sic" : "SIC290",
  "expiration_date" : "05/02/2025",
  "uw_name" : "UW3"
} ]
```


## `POST /api/submissions/random`
Creates a random submission and inserts it to the database. It will be streamed to all clients
subscribed to live submissions.

### Responses
- Returns a `status 200` and a JSON with generated random submission

### Examples
```bash
curl -X POST localhost:8080/api/submissions/random 
```
```json
{
  "lines_of_coverage" : [ "Coverage4", "Coverage5", "Coverage4" ],
  "premium_usd" : 1549.0,
  "state" : "NC",
  "account_name" : "Account68725609",
  "status" : "Done",
  "effective_date" : "06/09/2024",
  "sic" : "SIC637",
  "expiration_date" : "27/05/2025",
  "uw_name" : "UW3"
}
```

## `GET api/submissions/live`
Starts a server push connection with the client that streams new
submissions as they are inserted into the the database

### Responses
The first response will be the string
```
event: subscribe-to-new-submissions!
data: success
```
After this events it will only respond with messages in the format below. 
Each line starting with `data:` holds a line of a JSON submission
```
event: new-submission
data: {
data:   "lines_of_coverage" : [ "Coverage3", "Coverage5", "Coverage3", "Coverage5" ],
data:   "premium_usd" : 1664.0,
data:   "state" : "GA",
data:   "account_name" : "Account85588291",
data:   "status" : "Done",
data:   "effective_date" : "26/11/2024",
data:   "sic" : "SIC428",
data:   "expiration_date" : "26/03/2025",
data:   "uw_name" : "UW4"
data: }
```

### Examples
```bash
curl -X GET http://localhost:8080/api/submissions/live
```
```
event: subscribe-to-new-submissions!
data: success

event: new-submission
data: {
data:   "lines_of_coverage" : [ "Coverage1", "Coverage2", "Coverage1", "Coverage2" ],
data:   "premium_usd" : 2832.0,
data:   "state" : "PA",
data:   "account_name" : "Account48266628",
data:   "status" : "New",
data:   "effective_date" : "06/04/2024",
data:   "sic" : "SIC884",
data:   "expiration_date" : "01/09/2025",
data:   "uw_name" : "UW3"
data: }

event: new-submission
data: {
data:   "lines_of_coverage" : [ "Coverage2", "Coverage5", "Coverage2" ],
data:   "premium_usd" : 3235.0,
data:   "state" : "MA",
data:   "account_name" : "Account97275350",
data:   "status" : "In Progress",
data:   "effective_date" : "30/06/2024",
data:   "sic" : "SIC160",
data:   "expiration_date" : "05/06/2025",
data:   "uw_name" : "UW2"
data: }
```
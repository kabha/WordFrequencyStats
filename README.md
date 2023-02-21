# wordFrequencyStats

create a service that consists of 2 endpoints:
The first endpoint should accept a string of comma-separated words.
The second endpoint should return stats about the frequency of the words received until this point in time:
Top 5 recurring words
Minimum frequency among all words
The median frequency
For example:
When the following inputs were received:

The response from the stats endpoint should be:

Task notes:
Do not use any external resources. Store data in memory.
Allow concurrency and expect a lot of traffic. Note that sending words to the server seems to happen much more than getting the
stats




API Tests : 
Use your favourite REST client to issue requests and check the output. For the simplistic use-case that it is, postman can also be used.



http://localhost:8080/api/v1/wordfrequency/addwords
{
    "requestStatus": "SUBMITTED",
    "jobId": "2cc8ffed-9890-4c8e-b3b8-bbf9a2198234",
    "wordFrequencyStats": null
}


http://localhost:8080/api/v1/wordfrequency/wordFrequencyStats/2af318c8-cf19-4714-a1bd-754e73f95fd9

http://localhost:8080/api/v1/wordfrequency/wordFrequencyStatsAll


{
    "minFrequency": 4,
    "medianFrequency": 4,
    "topFrequencies": [
        {
            "word": "error",
            "frequency": 12
        },
        {
            "word": "eggs",
            "frequency": 4
        },
        {
            "word": "daoud",
            "frequency": 4
        }
    ]
}

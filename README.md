hypermedia-play
=====================================

This is a demo application to show the creation of a hypermedia api with the Play Framework.
The goal is to add hypermedia support to a play application without having to add any link constraints or information to the domain model / entity.

**This is currently work in progress! Help, ideas and comments are welcome!**

### API

#### create: /task
```bash
curl --header "Content-type: application/json"  --request POST --data '{"taskId": "1", "title": "title", "sender": "sender", "recipients": ["a", "b"]}' http://localhost:9000/task
```

#### get json: /tasks
```bash
curl --header "Accept: application/json" --request GET http://localhost:9000/tasks | python -mjson.tool
```

```json
{
    "collection": [
        {
            "_links": [
                {
                    "mediaType": "application/json",
                    "rel": "delete",
                    "uri": "/task/12345"
                },
                {
                    "mediaType": "application/json",
                    "rel": "self",
                    "uri": "/task/12345"
                },
                {
                    "mediaType": "application/json",
                    "rel": "update",
                    "uri": "/task"
                }
            ],
            "recipients": [
                "recipientA",
                "recipientB"
            ],
            "sender": "sender1",
            "taskId": "12345",
            "title": "title1"
        },
        {
            "_links": [
                {
                    "mediaType": "application/json",
                    "rel": "delete",
                    "uri": "/task/12346"
                },
                {
                    "mediaType": "application/json",
                    "rel": "self",
                    "uri": "/task/12346"
                },
                {
                    "mediaType": "application/json",
                    "rel": "update",
                    "uri": "/task"
                }
            ],
            "recipients": [
                "recipientC",
                "recipientB"
            ],
            "sender": "sender2",
            "taskId": "12346",
            "title": "title2"
        }
    ]
}
```

#### get json: /task/:id
```bash
curl --header "Accept: application/json" --request GET http://localhost:9000/task/1 | python -mjson.tool
```

```json
{
    "_links": [
        {
            "mediaType": "application/json",
            "rel": "delete",
            "uri": "/task/12345"
        },
        {
            "mediaType": "application/json",
            "rel": "self",
            "uri": "/task/12345"
        },
        {
            "mediaType": "application/json",
            "rel": "update",
            "uri": "/task"
        }
    ],
    "recipients": [
        "recipientA",
        "recipientB"
    ],
    "sender": "sender",
    "taskId": "12345",
    "title": "title"
}
```

#### get xml: /task/:id
```bash
curl --header "Accept: application/xml" --request GET http://localhost:9000/task/1 | xmllint --format -
```

#### update	/task
```bash
curl --header "Content-type: application/json"  --request PUT --data '{"taskId": "1", "title": "title", "sender": "sender", "recipients": ["a", "b"]}' http://localhost:9000/task
```

#### delete	/task/:id
```bash
curl --request DELETE http://localhost:9000/task/1
```

### Resources
* [REST in Practice](http://restinpractice.com/)

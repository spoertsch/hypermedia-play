hypermedia-play
=====================================

This is a demo application to show the creation of a hypermedia api with the Play Framework.

**This is currently work in progress!**

### API

#### create: /task
```bash
curl --header "Content-type: application/json"  --request POST --data '{"taskId": "1", "title": "title", "sender": "sender", "recipients": ["a", "b"]}' http://localhost:9000/task
```

#### get json: /task/:id
```bash
curl --header "Accept: application/json" --request GET http://localhost:9000/task/1 | python -mjson.tool
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

hypermedia-play
=====================================

This is a proof of concept application to show the creation of a hypermedia api with the Play Framework.
The goal is to add hypermedia support to a play application without having to add any link constraints or information to the domain model / entity.

**This is currently work in progress! Help, ideas and comments are welcome!**

### How to use
There is no need to add any link specific information to your domain model. Only implicit (json) reads and writes need to be available.
The links will only be added to the object returned by the action by manipulating the json response.

#### Add links directly
It is possible to create the links manually as needed and add them to the returned object.

```scala
  def findById(id: String) = Action {
    implicit req =>
      val task: Task = Tasks.findById(id)

      render {
        case Accepts.Json() => {
          val links = Links()
          links.addUpdateLink(routes.TaskController.update.toString, "application/json");
          
          links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
          links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

          Ok(links.addAsJsonTo(task)).as(JSON)
        }
        case _ => NotAcceptable
      }
  }
```

#### Add links with generator function
A different possibility is to create a generator function that creates the link for a specific object. The links will then be created for that object by using this generator function.

```scala

  private def generateTaskLinks(task: Task): Links = {
    val links = Links()
    links.addUpdateLink(routes.TaskController.update.toString, "application/json");

    links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
    links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

    links
  }

  def findAll() = Action {
    implicit req =>
      val taskList = Tasks.findAll

      render {
        case Accepts.Json() => {
          Ok(Links.generateAsJson(taskList, generateTaskLinks _)).as(JSON)
        }
        case _ => NotAcceptable
      }
  }
```

#### Implicit generator function
This is the same approach as above but using an implicit generator function.

```scala

  implicit def generateTaskLinks(task: Task): Links = {
    val links = Links()
    links.addUpdateLink(routes.TaskController.update.toString, "application/json");

    links.add(linkTo(routes.TaskController.findById(task.taskId)).withSelfRel.withJsonMediaType)
    links.add(linkTo(routes.TaskController.delete(task.taskId)).withDeleteRel.withJsonMediaType)

    links
  }

  def findAll() = Action {
    implicit req =>
      val taskList = Tasks.findAll

      render {
        case Accepts.Json() => {
          Ok(Links.generateAsJsonImplicit(taskList)).as(JSON)
        }
        case _ => NotAcceptable
      }
  }
```

### Sample API

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

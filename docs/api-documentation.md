# Calculator API Documentation

## Overview

Calculator API with asynchronous processing via Kafka

**Version:** 1.0

## Endpoints

### POST /api/calculator/{operation}

Performs a basic mathematical operation using asynchronous processing via Kafka.

#### Parameters

| Name      | Located in | Description                                          | Required | Schema |
|-----------|------------|------------------------------------------------------|----------|---------|
| operation | path       | Mathematical operation (add, subtract, multiply, divide) | Yes      | string  |
| a         | query      | First number                                         | Yes      | number  |
| b         | query      | Second number                                        | Yes      | number  |

#### Responses

| Code | Description              |
|------|--------------------------|
| 200  | Operation successful     |
| 500  | Operation failed         |

### Operations Available

- `add`: Addition
- `subtract`: Subtraction
- `multiply`: Multiplication
- `divide`: Division

### Example Usage

```bash
curl -X POST "http://localhost:8084/api/calculator/add?a=10.5&b=5.2"
```

Example Response:
```json
{
    "result": 15.7
}
```

## Error Handling

In case of errors (HTTP 500), the response will be in the following format:
```json
{
  "error": "Operation timed out or failed"
}
```
# Generate a token
http://localhost:8080/security/generate/token?subject=one

{"result":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmUiLCJleHAiOjE1NDk4NjY0OTB9.uZsdPcefGJICGJo5Jbv0lXHbDRCZA3vWDPBp-op-5wM"}

# Get a subject from a JWT token
http://localhost:8080/security/get/subject?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmUiLCJleHAiOjE1NDk4NjY0OTB9.uZsdPcefGJICGJo5Jbv0lXHbDRCZA3vWDPBp-op-5wM

{"result":"one"}

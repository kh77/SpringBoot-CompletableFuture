### SpringBoot-CompletableFuture

- Create employee through file uploading, file is present in the resource folder 
- Get employee list
- Get employee list by name or age
- Upload multiple file at a time


    #### Dump employee data through csv file

    curl --location --request POST 'localhost:8080/api/employee' \
    --form 'files=@"/C:/Users/abc/Desktop/employee-1.csv"' \
    --form 'files=@"/C:/Users/abc/Desktop/employee-2.csv"'


    #### Get all employees

    curl --location --request GET 'localhost:8080/api/employee'


    #### Get all employees by Name Or Age 

    curl --location --request GET 'localhost:8080/api/employee/ali/16' 
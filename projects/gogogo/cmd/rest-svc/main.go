package main

import (
	"database/sql"
	"fmt"
	"log"
	"net/http"
	"os"

	"github.com/gorilla/mux"
	"rickiekarp.net/rickie/home/projects/gogogo/projects/gogogo/src/rickiekarp.net/command"
	"rickiekarp.net/rickie/home/projects/gogogo/projects/gogogo/src/rickiekarp.net/game/snakefx"
	"rickiekarp.net/rickie/home/projects/gogogo/projects/gogogo/src/rickiekarp.net/network"
	"rickiekarp.net/rickie/home/projects/gogogo/projects/gogogo/src/rickiekarp.net/parser/yamlparser"
)

const port = 8081

var db *sql.DB

func main() {
	argsWithProg := os.Args
	fmt.Println(argsWithProg)

	command.ExecuteCommandAndPrintResult()
	returnCode := command.ExecuteCommandAndGetExitCode()
	fmt.Println(returnCode)

	// read config
	var config yamlparser.Requestdata
	config.GetConf()
	fmt.Println(config)

	network.Get()

	db = snakefx.InitDB(fmt.Sprintf("%s:%s@tcp(%s:3306)/%s", config.DB.Username, config.DB.Password, config.DB.Url, config.DB.Database))

	router := mux.NewRouter()

	router.HandleFunc("/gamedata/highscore", snakefx.GetEmps).Methods("GET")
	router.HandleFunc("/gamedata/highscore/{name}", snakefx.GetEmp).Methods("GET")
	router.HandleFunc("/gamedata/addHighscore", snakefx.CreateEmp).Methods("POST")
	router.HandleFunc("/gamedata/highscore/{name}", snakefx.DeleteEmp).Methods("DELETE")

	fmt.Println("Start listening on", port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%d", port), router))
}

// Test:
// curl -d '{"name":"Test", "points":"1"}' -H "Content-Type: application/json" -X POST http://localhost:8080/gamedata/addHighscore

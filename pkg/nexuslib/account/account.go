package account

import (
	"bytes"
	"encoding/gob"
	"fmt"
	"log"
	"os"

	"git.rickiekarp.net/rickie/home/pkg/util"
	"github.com/sirupsen/logrus"
)

type Account struct {
	Id string
}

const accountFile = "profile.dat"

func Generate() (*Account, error) {
	// Create an instance of the account struct
	account := Account{Id: util.RandomString(34)}

	// Create a new buffer to write the serialized data to
	var b bytes.Buffer

	// Create a new gob encoder and use it to encode the account struct
	enc := gob.NewEncoder(&b)
	if err := enc.Encode(account); err != nil {
		fmt.Println("Error encoding struct:", err)
		return nil, err
	}

	// The serialized data can now be found in the buffer
	serializedData := b.Bytes()

	err := os.WriteFile(accountFile, serializedData, 0644)
	if err != nil {
		logrus.Println("Could not write file", accountFile)
	}
	return &account, nil
}

func Load() (*Account, error) {

	file, err := os.Open(accountFile)
	if err != nil {
		fmt.Println(err)
		return nil, err
	}
	defer file.Close()

	// The serialized data to be deserialized
	serializedData, err := os.ReadFile(accountFile)
	if err != nil {
		log.Fatal(err)
	}

	// Create a new buffer from the serialized data
	b := bytes.NewBuffer(serializedData)

	// Create a new gob decoder and use it to decode the account struct
	var account Account
	dec := gob.NewDecoder(b)
	if err := dec.Decode(&account); err != nil {
		fmt.Println("Error decoding struct:", err)
		return nil, err
	}

	// The account struct has now been deserialized
	return &account, nil
}

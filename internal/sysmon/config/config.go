package config

import (
	"io/ioutil"
	"os"

	"git.rickiekarp.net/rickie/home/pkg/database"
	"git.rickiekarp.net/rickie/home/pkg/models"
	"git.rickiekarp.net/rickie/home/pkg/monitoring/graphite"
	"github.com/sirupsen/logrus"
	"gopkg.in/yaml.v2"
)

type SysmonConfig struct {
	ServerAddr string `yaml:"serverAddr"`
	Graphite   struct {
		Host string `json:"host"`
		Port int    `json:"port"`
	}
	Databases []models.Database `yaml:"databases"`
}

var ConfigBaseDir string
var SysmonConf SysmonConfig
var Hostname string

// ReadSysmonConfig reads the given config file and tries to unmarshal it into the given configStruct
func ReadSysmonConfig() error {

	// get hostname (used for sending graphite metric)
	hostname, err := os.Hostname()
	if err != nil {
		logrus.Error("Could not determine hostname! ", err)
		return err
	}
	Hostname = hostname

	// read configfile
	yamlFile, err := ioutil.ReadFile(ConfigBaseDir + "config.yaml")
	if err != nil {
		logrus.Error("yamlFile.Get err: ", err)
		return err
	}

	// unmarshal config file depending on given configStruct
	err = yaml.Unmarshal(yamlFile, &SysmonConf)
	if err != nil {
		logrus.Error("Unmarshal failed: ", err)
		return err
	}

	database.Databases = SysmonConf.Databases

	// set graphite target config
	graphite.Host = SysmonConf.Graphite.Host
	graphite.Port = SysmonConf.Graphite.Port

	return nil
}

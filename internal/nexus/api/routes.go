package api

import (
	"git.rickiekarp.net/rickie/home/internal/nexus/api/routes"
	"git.rickiekarp.net/rickie/home/internal/nexus/blockchain"
	"git.rickiekarp.net/rickie/home/internal/nexus/channel"
	"git.rickiekarp.net/rickie/home/internal/nexus/config"
	"git.rickiekarp.net/rickie/home/internal/nexus/hub"
	"git.rickiekarp.net/rickie/home/internal/nexus/mail"
	"git.rickiekarp.net/rickie/home/internal/nexus/users"
	"git.rickiekarp.net/rickie/home/internal/nexus/webpage"
	"github.com/gorilla/mux"
)

// defineApiEndpoints defines all routes the Router can handle
func defineApiEndpoints(r *mux.Router) {
	r.HandleFunc("/", routes.ServeHome)
	r.HandleFunc("/ws", routes.ServeWebSocket)
	r.HandleFunc("/version", routes.ServeVersion).Methods("GET")
	r.HandleFunc("/hub/v1/preferences", routes.PatchPreferencesChanged).Methods("PATCH")
	r.HandleFunc("/hub/v1/send", hub.SendMessage).Methods("POST")
	r.HandleFunc("/hub/v1/broadcast", hub.BroadcastMessage).Methods("POST")

	// blockchain
	if config.NexusConf.NexusChain.Enabled {
		r.HandleFunc("/blockchain/get", blockchain.HandleGetBlockchain).Methods("GET")
		r.HandleFunc("/blockchain/addNode", blockchain.AddNewNodeToChain).Methods("GET")
		r.HandleFunc("/blockchain/addBlock", blockchain.AddNewBlock).Methods("GET")
	}

	// login service
	r.HandleFunc("/users/v1/authorize", users.Authorize).Methods("POST")
	r.HandleFunc("/users/v1/login", users.Login).Methods("POST")
	r.HandleFunc("/users/v1/register", users.Register).Methods("POST")

	// webpage service
	r.HandleFunc("/webpage/v1/contact", webpage.ServeContactInfo).Methods("GET")
	r.HandleFunc("/webpage/v1/resume/experience", webpage.ServeExperience).Methods("GET")
	r.HandleFunc("/webpage/v1/resume/education", webpage.ServeEducation).Methods("GET")
	r.HandleFunc("/webpage/v1/resume/skills", webpage.ServeSkills).Methods("GET")

	// mail service
	r.HandleFunc("/mail/v2/notify", mail.Notify).Methods("POST")
	r.HandleFunc("/mail/v1/notify/reminders", mail.NotifyRemindersEndpoint).Methods("POST")

	r.HandleFunc("/modules/weather/stop", channel.StopWeatherMonitorEndpoint).Methods("GET")
	r.HandleFunc("/modules/weather/start", channel.StartWeatherMonitorEndpoint).Methods("GET")
	r.HandleFunc("/modules/weather/status", channel.WeatherMonitorStatusEndpoint).Methods("GET")

	r.HandleFunc("/monitoring/notifyUptime", NotifyUptimeEndpoint).Methods("POST")
	r.HandleFunc("/monitoring/notifyTemperature", NotifyTemperatureEndpoint).Methods("POST")
}

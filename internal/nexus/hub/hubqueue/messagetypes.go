package hubqueue

import "git.rickiekarp.net/rickie/home/pkg/queue"

const (
	FilestoreAdd       queue.QueueEventType = "filestore_add"
	FilestoreAddExtras queue.QueueEventType = "filestore_add_extras"
)

type IQueueEventMessage interface {
	FileStorageEventMessage | FileStorageAdditionalDataEventMessage
}

type FileStorageEventMessage struct {
	Id             *int64                                   `json:"id,omitempty"`
	Path           string                                   `json:"path,omitempty"`
	Name           string                                   `json:"name,omitempty"`
	Size           int64                                    `json:"size,omitempty"`
	Mtime          int64                                    `json:"mtime,omitempty"`
	Checksum       string                                   `json:"checksum,omitempty"`
	Owner          string                                   `json:"owner,omitempty"`
	Inserttime     *int64                                   `json:"inserttime,omitempty"`
	Lastupdate     *int64                                   `json:"lastupdate,omitempty"`
	AdditionalData *[]FileStorageAdditionalDataEventMessage `json:"additional_data,omitempty"`
}

type FileStorageAdditionalDataEventMessage struct {
	FilesId  *int64 `json:"files_id,omitempty"`
	Property string `json:"property,omitempty"`
	Value    string `json:"value,omitempty"`
}

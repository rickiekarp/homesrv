GOCMD=go
GOBUILD=$(GOCMD) build
GOCLEAN=$(GOCMD) clean
GOTEST=$(GOCMD) test
GOGET=$(GOCMD) get
BUILD_PATH=build
BINARY_NAME=app
DEPLOY_DIR=/mnt/raid2/nodes/raspberrypi/deployment/

# Nucleus

buildNucleusARM64v7:
		CGO_ENABLED=0 GOOS=linux GOARCH=arm64 GOARM=7 \
		go build -ldflags="-X git.rickiekarp.net/rickie/home/internal/nucleus/config.Version=$(shell date +%Y%j%H%m) \
		-X git.rickiekarp.net/rickie/home/internal/nucleus/config.Build=$(shell git rev-parse HEAD) \
		-X git.rickiekarp.net/rickie/home/internal/nucleus/config.ConfigBaseDir=data/config/ \
		-X git.rickiekarp.net/rickie/home/internal/nucleus/config.ResourcesBaseDir=data/assets/web/" \
		-o $(BUILD_PATH)/output/nucleus/$(BINARY_NAME) \
		cmd/nucleus/main.go
		mkdir -p $(BUILD_PATH)/output/nucleus/data/assets/web
		cp -r web/nucleus/* $(BUILD_PATH)/output/nucleus/data/assets/web
		cp -r deployments/module-deployment/values/nucleus/prod/* $(BUILD_PATH)/output/nucleus/data/
		cp deployments/docker/Dockerfile_goscratch build/output/nucleus/Dockerfile

deployNucleus:
		rsync -rlvpt --delete build/output/nucleus pi:$(DEPLOY_DIR)

# Project6

## AMD64

buildProject6AMD64:
		CGO_ENABLED=0 GOOS=linux GOARCH=amd64 \
		go build -ldflags="-X git.rickiekarp.net/rickie/home/internal/project6/config.Version=$(shell date +%Y%j%H%m) \
		-X git.rickiekarp.net/rickie/home/internal/project6/config.Build=$(shell git rev-parse HEAD) \
		-X git.rickiekarp.net/rickie/home/internal/project6/config.ConfigBaseDir=data/config/" \
		-o $(BUILD_PATH)/output/project6/$(BINARY_NAME) \
		cmd/project6/main.go

deployProject6AMD64:
		rsync -rlvpt --delete build/output/project6/app pi@rickiekarp.net:/mnt/raid2/applications/nginx/files/software/dev/project6/amd64/project6

## ARM64

buildProject6ARM64:
		CGO_ENABLED=0 GOOS=linux GOARCH=arm64 \
		go build -ldflags="-X git.rickiekarp.net/rickie/home/internal/project6/config.Version=$(shell date +%Y%j%H%m) \
		-X git.rickiekarp.net/rickie/home/internal/project6/config.Build=$(shell git rev-parse HEAD) \
		-X git.rickiekarp.net/rickie/home/internal/project6/config.ConfigBaseDir=data/config/" \
		-o $(BUILD_PATH)/output/project6/$(BINARY_NAME) \
		cmd/project6/main.go

deployProject6ARM64:
		rsync -rlvpt --delete build/output/project6/app pi@rickiekarp.net:/mnt/raid2/applications/nginx/files/software/dev/project6/arm64/project6

# Other

test:
		$(GOTEST) -v ./...

clean:
		$(GOCLEAN)
		rm -rf $(BUILD_PATH)

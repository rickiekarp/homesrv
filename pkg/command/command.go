package command

import (
	"bytes"
	"log"
	"os/exec"
	"strings"
	"syscall"
)

// ExecuteCmd executes a given system command
// It returns the Stdout AND Stderr buffer as a string and the exit code
func ExecuteCmd(command string, args ...string) (string, string, int) {

	var stdout bytes.Buffer
	var stderr bytes.Buffer

	cmd := exec.Command(command, args...)
	cmd.Stdout = &stdout
	cmd.Stderr = &stderr

	err := cmd.Start()
	if err != nil {
		log.Printf("cmd.Start() failed with '%s'\n", err)
		return "", "", 1
	}

	err = cmd.Wait()
	if err != nil {
		log.Printf("cmd.Run() failed with %s\n", err)

		if exiterr, ok := err.(*exec.ExitError); ok {
			// The program has exited with an exit code != 0

			// This works on both Unix and Windows. Although package
			// syscall is generally platform dependent, WaitStatus is
			// defined for both Unix and Windows and in both cases has
			// an ExitStatus() method with the same signature.
			if status, ok := exiterr.Sys().(syscall.WaitStatus); ok {
				log.Printf("Exit Status: %d", status.ExitStatus())
				return strings.TrimSuffix(string(stdout.String()), "\n"),
					strings.TrimSuffix(string(stderr.String()), "\n"),
					status.ExitStatus()
			}
		}
	}

	return strings.TrimSuffix(string(stdout.String()), "\n"),
		strings.TrimSuffix(string(stderr.String()), "\n"),
		0
}

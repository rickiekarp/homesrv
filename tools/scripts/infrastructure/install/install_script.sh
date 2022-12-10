#! /bin/bash
echo "Software Install Script for Ubuntu 22.10"
echo "Author: Rickie Karp (contact@rickiekarp.net)"

idx=0

# main option selection
print_options()
{
	echo "1: Easy Install (All options)"
	echo "2: Uninstall redundant software"
	echo "3: Install software"
	echo "4: Install development software"
	echo "5: Install games"
	echo "6: Check for software updates"
	echo "7: Exit"
}

# main option selection
select_option()
{
	echo "Please enter your option:"
	read INDEX

	case $INDEX in
		1) do_all ;;
		2) uninstall_software ;;
		3) install_software ;;
		4) install_dev_software ;;
		5) install_games ;;
		6) check_for_updates ;;
		7) exit ;;

		*) echo "INVALID NUMBER!" ;;
	esac
}

# uninstalls redundant software
uninstall_software()
{
	echo "Uninstalling not needed software..."
	sudo apt-get --purge remove -y rhythmbox rhythmbox-data librhythmbox-core10

	sudo apt -y autoremove	#removes packages that were installed by other packages and are no longer needed

	check_process_exit
}

# installs software from default repository
install_software()
{
	echo "Installing libraries..."
	sudo apt install -y libfuse2
	# virtual camera support
	sudo apt install -y v4l2loopback-dkms
	echo "Installing software..."
	snap install opera
	sudo apt install -y keepassxc guake vlc steam bleachbit gimp \
		vim curl ffmpeg timeshift obs-studio gnome-shell-extension-manager easytag darktable rawtherapee
	snap install telegram-desktop
	snap install discord

	check_process_exit
}

# installs development related software from default repository
install_dev_software()
{
	echo "Installing development software..."
	sudo apt install -y git openjdk-17-jdk virtualbox virtualbox-qt adb jmeter jmeter-http

	# Install Docker
	sudo apt install -y docker docker.io docker-compose
	sudo usermod -a -G docker $USER

	# Install Visual Studio Code
	snap install --classic code

	check_process_exit
}

# installs games from default repository
install_games()
{
	echo "Installing games..."
	sudo apt install -y warzone2100 warzone2100-music

	check_process_exit
}

# easy install function
do_all()
{
	read -r -p "This option will execute all other listed functions. Continue? [y/n]" response
	case $response in 
	   [yY][eE][sS]|[yY]) 
		let idx=1
		uninstall_software
		check_for_updates
		install_software
		install_dev_software
		install_games
		echo "DONE!"
	        ;;
	   *)
		echo "Exiting..."
		process_exit
 	       ;;
	esac
}

# checks the system for updates and installs them
check_for_updates()
{
	echo "Checking for software updates..."
	sudo apt-get update && sudo apt-get -y upgrade
}

# checks $idx variable
# if idx != 1, exit the program
check_process_exit()
{
	if [[ $idx -ne 1 ]]; then
		process_exit
	fi
}

# program exit selection
process_exit()
{
	read -r -p "Do you want to do something else? [y/n]" response
	case $response in 
	   [yY][eE][sS]|[yY]) 
		let idx=0
		print_options
		select_option
	        ;;
	   *)
		echo "DONE!"
 	       ;;
	esac
}

print_options
select_option



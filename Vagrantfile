Vagrant.configure("2") do |config|
    config.vm.box = 'ATGMemcached'
    config.vm.box_url = 'http://files.vagrantup.com/precise64.box'
    config.ssh.forward_agent = true
    config.vm.network :forwarded_port, guest: 11211, host: 11211, auto_correct: true
    config.vm.provision :puppet do |puppet|
        puppet.manifests_path = "puppet/manifests"
        puppet.manifest_file  = "base.pp"
        puppet.module_path = "puppet/modules"
        #puppet.options = "--verbose --debug"
    end
end
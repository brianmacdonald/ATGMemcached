class memcached {

  $memcached_conf = "/etc/memcached.conf"

  package { 'memcached':
    ensure => 'latest'
  }

  service { 'memcached':
    ensure => running,
    enable => true,
    require => Package['memcached'],
  }

  file_line { 'Memcached conf update.':
    path => '/etc/memcached.conf',
    line => '-l 0.0.0.0',
    match => '^-l.*',
  }

  exec { 'Reload memcache config.':
    command => '/etc/init.d/memcached force-reload',
  }

}
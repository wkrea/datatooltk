#!/usr/bin/perl -w

use strict;
use Tk;
use Tk::PNG;
use DatatoolTk;

my $db = DatatoolTk->new();

my $selectedRow = $db->selectedRow();

my %colIndexes = ();

foreach my $key (qw/ID Surname Forename Title Address Telephone
Email/)
{
   my $idx = $db->getColumnIndex($key);

   if ($idx == -1)
   {
      die $db->getDictWord('plugin.error.missing_column', $key),
"\n";
   }

   $colIndexes{$key} = $idx;
}

my @row = ("") x $db->columnCount;

if ($selectedRow > -1)
{
   @row = @{$db->getRow($selectedRow)};

   $row[$colIndexes{Address}]=~s/\\\\/\n/sg;
}

my $mw = MainWindow->new;

$mw->title($selectedRow > -1 ? 
  &getWord("edit_entry") : &getWord("new_entry"));

my $nameFrame = $mw->Frame()->pack;

$nameFrame->Label
  (
    -text => &getWord('Title')
  )->pack(-side=>'left', -expand=>1);

my $titleEntry = $nameFrame->Entry
  (
    -width=>4,
  )->pack(-side=>'left', -expand=>1);

$titleEntry->insert(0, $row[$colIndexes{Title}]);

$nameFrame->Label
  (
    -text => &getWord('Forename')
  )->pack(-side=>'left', -expand=>1);

my $forenameEntry = $nameFrame->Entry()->pack(-side=>'left', -expand=>1);

$forenameEntry->insert(0, $row[$colIndexes{Forename}]);

$nameFrame->Label
  (
    -text => &getWord('Surname')
  )->pack(-side=>'left', -expand=>1);

my $surnameEntry = $nameFrame->Entry()->pack(-side=>'left', -expand=>1);

$surnameEntry->insert(0, $row[$colIndexes{Surname}]);

my $contactFrame = $mw->Frame()->pack;

$contactFrame->Label
  (
    -text=>&getWord('Email')
  )->pack(-side=>'left', -expand=>1);

my $emailEntry = $contactFrame->Entry()->pack(-side=>'left', -expand=>1);

$emailEntry->insert(0, $row[$colIndexes{Email}]);

$contactFrame->Label
  (
    -text=>&getWord('Telephone')
  )->pack(-side=>'left', -expand=>1);

my $telephoneEntry = $contactFrame->Entry()->pack(-side=>'left', -expand=>1);

$telephoneEntry->insert(0, $row[$colIndexes{Telephone}]);

my $addressFrame = $mw->Frame()->pack();

$addressFrame->Label
  (
    -text=>&getWord('Address')
  )->pack(-side=>'left', -expand=>1);

my $addressText = $addressFrame->Scrolled('Text', -width=>40, -height=>6)->pack();

$addressText->Contents($row[$colIndexes{Address}]);

my $buttonFrame = $mw->Frame()->pack;

my $shot;

my $imgFile = $db->getImageFile('cancel.png');

if ($imgFile and -e $imgFile)
{
   $shot = $mw->Photo(-file=>$imgFile);

   $buttonFrame->Button(
       -text     => $db->getDictWord('button.cancel'),
       -command  => sub { exit },
       -image    => $shot,
       -compound => 'left'
   )->pack(-side=>'left', -expand=>1);
}
else
{
   $buttonFrame->Button(
       -text    => $db->getDictWord('button.cancel'),
       -command => sub { exit },
   )->pack(-side=>'left', -expand=>1);
}

$imgFile = $db->getImageFile('okay.png');

if ($imgFile and -e $imgFile)
{
   $shot = $mw->Photo(-file=>$imgFile);

   $buttonFrame->Button(
      -text    => $db->getDictWord('button.okay'),
      -command => \&doDbUpdate,
      -image    => $shot,
      -compound => 'left'
   )->pack(-side=>'left', -expand=>1);
}
else
{
   $buttonFrame->Button(
      -text    => $db->getDictWord('button.okay'),
      -command => \&doDbUpdate,
   )->pack(-side=>'left', -expand=>1);
}

$mw->update;

my $xpos = int(($mw->screenwidth-$mw->width)/2);
my $ypos = int(($mw->screenheight-$mw->height)/2);

$mw->geometry("+$xpos+$ypos");

MainLoop;

sub doDbUpdate{

  $row[$colIndexes{Title}] = $titleEntry->get;

  $row[$colIndexes{Surname}] = $surnameEntry->get;

  $row[$colIndexes{Forename}] = $forenameEntry->get;

  $row[$colIndexes{Email}] = $emailEntry->get;

  $row[$colIndexes{Telephone}] = $telephoneEntry->get;

  $row[$colIndexes{Address}] = $addressText->Contents;

  $row[$colIndexes{Address}]=~s/\n\s*$//;

  $row[$colIndexes{Address}]=~s/\n/\\\\<br\/>/sg;

  $db->startModifications;

  if ($selectedRow > -1)
  {
     $db->replaceRow($selectedRow, \@row);
  }
  else
  {
     $row[$colIndexes{ID}] = $db->maxForColumn($colIndexes{ID})+1;

     $db->appendRow(\@row);
  }

  $db->endModifications;

  exit;
}

sub getWord{
  $db->getDictWord('plugin.people.'.shift)
}

1;
